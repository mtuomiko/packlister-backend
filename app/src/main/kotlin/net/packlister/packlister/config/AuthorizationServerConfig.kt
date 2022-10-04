package net.packlister.packlister.config

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import net.packlister.packlister.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.OAuth2TokenType
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.time.Duration

@Configuration
@EnableConfigurationProperties(AuthorizationServerProperties::class)
class AuthorizationServerConfig(
    @Autowired
    private val authorizationServerProperties: AuthorizationServerProperties
) {
    /**
     * Configure authorization server.
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerFilterChain(http: HttpSecurity): SecurityFilterChain {
        // any request on authorization server endpoints must be authorized
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)

        http.invoke {
            exceptionHandling { authenticationEntryPoint = LoginUrlAuthenticationEntryPoint("/login") }
            oauth2ResourceServer { jwt { } } // allows, for example, userinfo endpoint usage using the access tokens
        }

        return http.build()
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    /**
     * Insert user UUID identifier into access token. Subsequent calls using the access token will trust the user id
     * and not query database (for example, to get to user id based on username already available in the token).
     */
    @Bean
    fun tokenCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer<JwtEncodingContext> { context ->
            val principal = context.getPrincipal<Authentication>()
            if (context.tokenType == OAuth2TokenType.ACCESS_TOKEN && principal is UsernamePasswordAuthenticationToken) {
                val user = principal.principal as User
                context.claims.claim("user_id", user.id.toString())
            }
        }
    }

    /**
     * Using
     */
    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val registeredClient = RegisteredClient.withId(REGISTERED_CLIENT_ID)
            .clientId(authorizationServerProperties.clientId)
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUris { yolo -> yolo.addAll(authorizationServerProperties.allowedRedirectURIs) }
            .scope(OidcScopes.OPENID)
            .clientSettings(clientSettings())
            .tokenSettings(tokenSettings())
            .build()
        return InMemoryRegisteredClientRepository(registeredClient)
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val jwk = JWK.parseFromPEMEncodedObjects(authorizationServerProperties.rsaPrivateKey)
        val jwkSet = JWKSet(jwk)
        return JWKSource<SecurityContext> { jwkSelector, _ -> jwkSelector.select(jwkSet) }
    }

    @Bean
    fun providerSettings(): ProviderSettings {
        return ProviderSettings.builder().issuer(authorizationServerProperties.issuer).build()
    }

    // Require Proof Key for Code Exchange (PKCE) use for the authorization code flow
    private fun clientSettings() = ClientSettings.builder().requireProofKey(true).build()

    private fun tokenSettings() =
        TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(TOKEN_VALIDITY_TIME)).build()
}

// Not used externally, so could be random as well.
const val REGISTERED_CLIENT_ID = "63c0c883-4a8c-43bd-aeff-95bc00bd52f8"

const val TOKEN_VALIDITY_TIME = 365L
