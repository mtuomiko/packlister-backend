package net.packlister.packlister.svc

import net.packlister.packlister.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AuthService(
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val jwtEncoder: JwtEncoder
) {
    fun token(username: String, password: String): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )
        val user = authentication.principal as User

        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
            .issuer("packlister")
            .subject(username)
            .claim("user_id", user.id.toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(VALIDITY))
            .build()
        val header = JwsHeader.with(MacAlgorithm.HS256).build()
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).tokenValue
    }
}

const val VALIDITY = 86400L
