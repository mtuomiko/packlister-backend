package net.packlister.packlister.config

import net.packlister.packlister.model.CustomClaim
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimValidator
import org.springframework.security.oauth2.jwt.JwtValidators
import java.util.UUID

object TokenValidators {
    // check that user_id is present
    private val userIdValidator = JwtClaimValidator<String>(CustomClaim.USER_ID.key) { userId -> isUUID(userId) }

    // has refresh token family uuid meaning it's a refresh token (if otherwise valid)
    private val isRefreshTokenValidator =
        JwtClaimValidator<String>(CustomClaim.REFRESH_TOKEN_FAMILY.key) { family -> isUUID(family) }

    // does not have refresh token family uuid meaning it's an access token (if otherwise valid)
    private val isAccessTokenValidator =
        JwtClaimValidator<String?>(CustomClaim.REFRESH_TOKEN_FAMILY.key) { family -> family == null }

    fun accessTokenValidator(issuer: String): OAuth2TokenValidator<Jwt> {
        return DelegatingOAuth2TokenValidator(
            JwtValidators.createDefaultWithIssuer(issuer),
            userIdValidator,
            isAccessTokenValidator
        )
    }

    fun refreshTokenValidator(issuer: String): OAuth2TokenValidator<Jwt> {
        return DelegatingOAuth2TokenValidator(
            JwtValidators.createDefaultWithIssuer(issuer),
            userIdValidator,
            isRefreshTokenValidator
        )
    }

    @Suppress("SwallowedException")
    private fun isUUID(input: String): Boolean {
        return try {
            UUID.fromString(input)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
