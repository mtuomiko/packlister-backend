package net.packlister.packlister.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@ConstructorBinding
@ConfigurationProperties(prefix = "packlister.authserver")
@Validated
class AuthorizationServerProperties(
    @field:NotEmpty
    val clientId: String,
    @field:NotEmpty
    val issuer: String,
    @field:NotEmpty
    val allowedRedirectURIs: List<String>,
    rsaPrivateKey: String
) {
    /**
     * Support multiline keys where newlines have been escaped with a single line feed. Allows condensing the PEM key
     * to a single line env var. Originally made for simple usage of .env files using docker run but might be useful in
     * production environments as well.
     */
    @field:NotEmpty
    val rsaPrivateKey: String = rsaPrivateKey.replace("\\n", "\n")
}
