object Libs {
    object Spring {
        const val bootStarterWeb = "org.springframework.boot:spring-boot-starter-web"
        const val bootStarterDataJpa = "org.springframework.boot:spring-boot-starter-data-jpa"
        const val bootStarterActuator = "org.springframework.boot:spring-boot-starter-actuator"
        const val bootStarterSecurity = "org.springframework.boot:spring-boot-starter-security"
        const val bootStarterTest = "org.springframework.boot:spring-boot-starter-test"
        const val bootStarterOauthResourceServer = "org.springframework.boot:spring-boot-starter-oauth2-resource-server"
    }

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"

    const val jacksonModuleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"

    const val flywayCore = "org.flywaydb:flyway-core"
    const val postgres = "org.postgresql:postgresql"
    const val hibernateTypes = "com.vladmihalcea:hibernate-types-55"

    const val swaggerAnnotations = "io.swagger.core.v3:swagger-annotations"
    const val javaxValidationApi = "javax.validation:validation-api"

    const val mockitoCore = "org.mockito:mockito-core"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin"
}

object Versions {
    const val kotlin = "1.6.21" // change version also in buildSrc/build.gradle.kts

    const val springBoot = "2.7.3"

    const val hibernateTypes = "2.19.2"

    const val openApiGenerator = "6.0.1"
    const val swaggerAnnotations = "2.2.2"

    const val detekt = "1.21.0"
    const val spotless = "6.10.0"
    const val ktlint = "0.46.1"

    const val mockito = "4.0.0"
}

object Plugins {
    const val springBoot = "org.springframework.boot"

    const val detekt = "io.gitlab.arturbosch.detekt"
    const val spotless = "com.diffplug.spotless"
    const val openApiGenerator = "org.openapi.generator"
}
