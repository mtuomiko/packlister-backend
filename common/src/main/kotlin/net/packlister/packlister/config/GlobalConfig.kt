package net.packlister.packlister.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.InstantSource

@Configuration
class GlobalConfig {
    /**
     * Made globally available since used in app configuration and on service level
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    /**
     * Explicit Jackson ObjectMapper configuration. Hopefully avoids any confusion resulting from the Spring default
     * configuration in the future.
     */
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(REFLECTION_CACHE_SIZE)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
    }

    /**
     * Interface for accessing clock without any extraneous timezone features. Any domain logic will probably not have
     * any need for handling timezones (they are representational/UI related). This bean should also help with testing.
     * */
    @Bean
    fun instantSource(): InstantSource {
        return InstantSource.system()
    }
}

const val REFLECTION_CACHE_SIZE = 512
