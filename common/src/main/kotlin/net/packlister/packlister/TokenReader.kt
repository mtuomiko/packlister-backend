package net.packlister.packlister

import net.packlister.packlister.model.CustomClaim
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TokenReader {
    fun getUserId(): UUID {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val token = authentication.token
        val claims = token.claims
        val userId = claims[CustomClaim.USER_ID.key] as String
        return UUID.fromString(userId)
    }
}
