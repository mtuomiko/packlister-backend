package net.packlister.packlister.svc

import mu.KotlinLogging
import net.packlister.packlister.config.AuthConfigProperties
import net.packlister.packlister.dao.RefreshTokenDao
import net.packlister.packlister.dao.UserDao
import net.packlister.packlister.dao.model.RefreshToken
import net.packlister.packlister.model.CustomClaim
import net.packlister.packlister.model.UnauthorizedError
import net.packlister.packlister.model.User
import net.packlister.packlister.svc.model.TokensWithUserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.InstantSource
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Component
class AuthService(
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val jwtEncoder: JwtEncoder,
    @Autowired private val refreshTokenDao: RefreshTokenDao,
    @Autowired private val userDao: UserDao,
    @Autowired private val authConfigProperties: AuthConfigProperties,
    @Autowired private val instantSource: InstantSource
) {
    fun token(username: String, password: String): TokensWithUserInfo {
        val user = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        ).principal as User

        val now = instantSource.instant()
        val tokenFamily = UUID.randomUUID()
        val tokenPair = createTokens(user, tokenFamily, now)

        refreshTokenDao.create(
            RefreshToken(
                id = UUID.randomUUID(),
                token = tokenPair.refreshToken,
                family = tokenFamily,
                expiresAt = now.plusSeconds(REFRESH_TOKEN_VALIDITY_SECONDS),
                userId = user.id
            )
        )

        return TokensWithUserInfo(tokenPair.accessToken, tokenPair.refreshToken, user.id, user.username, user.email)
    }

    /**
     *
     */
    @Suppress("ThrowsCount")
    fun token(refreshToken: Jwt): TokensWithUserInfo {
        val user = userDao.getByUsername(refreshToken.subject) ?: throw UnauthorizedError("no user found")
        if (!user.active) throw UnauthorizedError("user inactive")

        val tokenFamily = UUID.fromString(refreshToken.claims[CustomClaim.REFRESH_TOKEN_FAMILY.key] as String)

        // if no stored token found for valid refresh token family, presumably reuse check was triggered
        val storedToken = refreshTokenDao.getByTokenFamily(tokenFamily)
            ?: throw UnauthorizedError("no stored token found")

        val now = instantSource.instant()
        if (storedToken.token != refreshToken.tokenValue) {
            refreshTokenDao.deleteById(storedToken.id)
            throw UnauthorizedError("refresh token not matching")
        }
        if (storedToken.expiresAt.isBefore(now)) {
            refreshTokenDao.deleteById(storedToken.id)
            throw UnauthorizedError("refresh token expired")
        }

        val tokenPair = createTokens(user, tokenFamily, now)

        refreshTokenDao.deleteById(storedToken.id)
        refreshTokenDao.create(
            RefreshToken(
                id = UUID.randomUUID(),
                token = tokenPair.refreshToken,
                family = tokenFamily,
                expiresAt = now.plusSeconds(REFRESH_TOKEN_VALIDITY_SECONDS),
                userId = user.id
            )
        )

        return TokensWithUserInfo(tokenPair.accessToken, tokenPair.refreshToken, user.id, user.username, user.email)
    }

    fun logout(refreshToken: Jwt) {
        val tokenFamily = UUID.fromString(refreshToken.claims[CustomClaim.REFRESH_TOKEN_FAMILY.key] as String)
        refreshTokenDao.deleteByTokenFamily(tokenFamily)
    }

    private fun createTokens(user: User, tokenFamily: UUID, now: Instant): TokenPair {
        val commonClaims = JwtClaimsSet.builder()
            .issuer(authConfigProperties.issuer)
            .subject(user.username)
            .claim(CustomClaim.USER_ID.key, user.id.toString())
            .issuedAt(now)
            .build()
        val accessTokenClaims = JwtClaimsSet.from(commonClaims)
            .expiresAt(now.plusSeconds(ACCESS_TOKEN_VALIDITY_SECONDS))
            .build()
        val refreshTokenClaims = JwtClaimsSet.from(commonClaims)
            .expiresAt(now.plusSeconds(REFRESH_TOKEN_VALIDITY_SECONDS))
            .claim(CustomClaim.REFRESH_TOKEN_FAMILY.key, tokenFamily.toString())
            .build()

        val header = JwsHeader.with(MacAlgorithm.HS256).build()
        val accessToken = jwtEncoder.encode(JwtEncoderParameters.from(header, accessTokenClaims)).tokenValue
        val refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(header, refreshTokenClaims)).tokenValue

        return TokenPair(accessToken, refreshToken)
    }

    /**
     * With current deployment setup, cron does not work (no way to trigger if instance is suspended). Instead, running
     * on startup.
     *
     * Alternative for running at 02:35 server time:
     * "@Scheduled(cron = "0 35 2 * * *")"
     */
    @Suppress("UnusedPrivateMember")
    @EventListener(ApplicationStartedEvent::class)
    private fun deleteExpiredRefreshTokens() {
        val deletedCount = refreshTokenDao.deleteExpired()
        logger.info { "Deleted $deletedCount expired refresh tokens" }
    }

    class TokenPair(val accessToken: String, val refreshToken: String)
}

const val ACCESS_TOKEN_VALIDITY_SECONDS = 3600L // 1 hour
const val REFRESH_TOKEN_VALIDITY_SECONDS = 7776000L // ~3 months
