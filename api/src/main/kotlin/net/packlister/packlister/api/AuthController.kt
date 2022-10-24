package net.packlister.packlister.api

import mu.KotlinLogging
import net.packlister.packlister.config.AuthConfigProperties
import net.packlister.packlister.generated.api.AuthApiDelegate
import net.packlister.packlister.generated.model.TokenResponse
import net.packlister.packlister.generated.model.UserCredentials
import net.packlister.packlister.model.BadRequestError
import net.packlister.packlister.svc.AuthService
import net.packlister.packlister.svc.REFRESH_TOKEN_VALIDITY_SECONDS
import net.packlister.packlister.svc.UserService
import net.packlister.packlister.svc.model.TokensWithUserInfo
import net.packlister.packlister.svc.model.UserRegistration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.web.bind.annotation.RestController
import net.packlister.packlister.generated.model.User as APIUser
import net.packlister.packlister.generated.model.UserRegistration as APIUserRegistration

private val logger = KotlinLogging.logger {}

@RestController
class AuthController(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val authService: AuthService,
    @Autowired
    @Qualifier("refreshTokenDecoder")
    private val refreshTokenJwtDecoder: JwtDecoder,
    @Autowired
    private val authConfigProperties: AuthConfigProperties
) : AuthApiDelegate {
    override fun register(userRegistration: APIUserRegistration): ResponseEntity<TokenResponse> {
        userService.register(
            UserRegistration(
                username = userRegistration.username,
                email = userRegistration.email,
                password = userRegistration.password
            )
        )
        val tokensWithUser = authService.token(userRegistration.username, userRegistration.password)
        logger.info { "new user registered" }
        return createTokenResponseEntity(tokensWithUser)
    }

    override fun token(refreshToken: String?, userCredentials: UserCredentials?): ResponseEntity<TokenResponse> {
        val tokensWithUser = if (userCredentials != null) {
            authService.token(userCredentials.username, userCredentials.password)
        } else if (refreshToken != null) {
            val verifiedRefreshToken = refreshTokenJwtDecoder.decode(refreshToken)
            authService.token(verifiedRefreshToken)
        } else {
            throw BadRequestError("provide either refresh token or user credentials")
        }

        return createTokenResponseEntity(tokensWithUser)
    }

    override fun logout(refreshToken: String): ResponseEntity<Void> {
        val verifiedRefreshToken = refreshTokenJwtDecoder.decode(refreshToken)

        authService.logout(verifiedRefreshToken)

        return ResponseEntity.ok().build()
    }

    private fun createTokenResponseEntity(tokensWithUserInfo: TokensWithUserInfo): ResponseEntity<TokenResponse> {
        val refreshTokenCookie = createRefreshTokenCookie(tokensWithUserInfo.refreshToken)
        val response = TokenResponse().apply {
            accessToken = tokensWithUserInfo.accessToken
            user = APIUser().apply {
                this.id = tokensWithUserInfo.userId
                this.username = tokensWithUserInfo.username
                this.email = tokensWithUserInfo.email
            }
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(response)
    }

    private fun createRefreshTokenCookie(refreshToken: String) = ResponseCookie
        .from("refresh_token", refreshToken)
        .maxAge(REFRESH_TOKEN_VALIDITY_SECONDS)
        .secure(authConfigProperties.secureCookie)
        .httpOnly(true)
        .sameSite("None")
        .path("/api/auth")
        .domain(authConfigProperties.domain)
        .build()
}
