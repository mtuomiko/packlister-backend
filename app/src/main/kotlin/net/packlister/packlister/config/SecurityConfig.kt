package net.packlister.packlister.config

import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import javax.crypto.spec.SecretKeySpec

@EnableWebSecurity
@EnableConfigurationProperties(AuthConfigProperties::class)
class SecurityConfig(
    @Autowired
    private val authConfigProperties: AuthConfigProperties
) {
    // Expose global authentication manager, used to authenticate user manually when issuing tokens
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    /**
     * Configure the application API.
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            authorizeRequests {
                // auth endpoints are either public or depend on manual refresh token verification
                authorize("/api/auth/*", permitAll)
                authorize("/actuator/health", permitAll)
                authorize(anyRequest, authenticated)
            }
            csrf { disable() }
            cors { }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            oauth2ResourceServer { jwt { } }
        }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(authConfigProperties.allowedOrigin)
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    @Primary
    fun jwtDecoder(): JwtDecoder {
        val decoder = NimbusJwtDecoder
            .withSecretKey(SecretKeySpec(authConfigProperties.jwtSecret, ALGORITHM_NAME))
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
        decoder.setJwtValidator(TokenValidators.accessTokenValidator(authConfigProperties.issuer))
        return decoder
    }

    @Bean
    @Qualifier("refreshTokenDecoder")
    fun refreshTokenJwtDecoder(): JwtDecoder {
        val decoder = NimbusJwtDecoder
            .withSecretKey(SecretKeySpec(authConfigProperties.jwtSecret, ALGORITHM_NAME))
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
        decoder.setJwtValidator(TokenValidators.refreshTokenValidator(authConfigProperties.issuer))
        return decoder
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val secretKey = SecretKeySpec(authConfigProperties.jwtSecret, ALGORITHM_NAME)
        val immutableSecret: JWKSource<SecurityContext> = ImmutableSecret(secretKey)
        return NimbusJwtEncoder(immutableSecret)
    }
}

const val ALGORITHM_NAME = "HmacSHA256"
