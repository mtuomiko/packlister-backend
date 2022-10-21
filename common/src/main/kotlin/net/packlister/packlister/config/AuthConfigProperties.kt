package net.packlister.packlister.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.util.Base64
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@ConstructorBinding
@ConfigurationProperties(prefix = "packlister.auth")
@Validated
class AuthConfigProperties(
    val secureCookie: Boolean,
    @field:NotEmpty
    val domain: String,
    @field:NotEmpty
    val issuer: String,
    @field:NotEmpty
    val allowedOrigin: String,
    jwtSecret: String
) {
    /**
     * Base64 encoded input string is decoded to a byte array for use with SecretKeySpec. ByteArray must have length 64
     * (512 bits) matching the block size of SHA-256.
     */
    @field:Size(min = 64, max = 64)
    val jwtSecret: ByteArray = Base64.getDecoder().decode(jwtSecret)
}
