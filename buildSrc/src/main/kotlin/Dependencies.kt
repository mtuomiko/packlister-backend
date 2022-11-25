import org.gradle.api.JavaVersion

object Libs {
    object Spring {
        const val bootStarterWeb = "org.springframework.boot:spring-boot-starter-web"
        const val bootStarterDataJpa = "org.springframework.boot:spring-boot-starter-data-jpa"
        const val bootStarterActuator = "org.springframework.boot:spring-boot-starter-actuator"
        const val bootStarterSecurity = "org.springframework.boot:spring-boot-starter-security"
        const val bootStarterTest = "org.springframework.boot:spring-boot-starter-test"
        const val bootStarterOauthResourceServer = "org.springframework.boot:spring-boot-starter-oauth2-resource-server"
        const val securityOauth2ResourceServer = "org.springframework.security:spring-security-oauth2-resource-server"
        const val securityOauth2Jose = "org.springframework.security:spring-security-oauth2-jose"
        const val bootStarterValidation = "org.springframework.boot:spring-boot-starter-validation"
    }

    const val slf4jApi = "org.slf4j:slf4j-api"
    const val microutilsKotlinLogging = "io.github.microutils:kotlin-logging-jvm"

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"

    const val jacksonModuleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"

    const val flywayCore = "org.flywaydb:flyway-core"
    const val postgres = "org.postgresql:postgresql"
    const val hibernateTypes = "com.vladmihalcea:hibernate-types-60"

    const val swaggerAnnotations = "io.swagger.core.v3:swagger-annotations"
    const val beanValidationApi = "jakarta.validation:jakarta.validation-api"

    // remove javax when generated api code module is removed
    const val javaxValidationApi = "javax.validation:jakarta.validation-api"

    const val mockitoCore = "org.mockito:mockito-core"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin"
}

object Versions {
    const val kotlin = "1.7.20" // change version also in buildSrc/build.gradle.kts

    const val springBoot = "3.0.0"

    const val hibernateTypes = "2.20.0"

    const val openApiGenerator = "6.2.1"
    const val swaggerAnnotations = "2.2.7"

    const val microutilsKotlinLogging = "3.0.4"

    const val detekt = "1.21.0"
    const val spotless = "6.10.0"
    const val ktlint = "0.46.1"

    // this version not published yet, check again in a few days(?)
//    const val mockito = "4.5.1"
//    const val mockitoKotlin = "4.1.0"
    const val mockito = "4.0.0"
    const val mockitoKotlin = "4.0.0"

    // remove javax when generated api code module is removed
    const val javaxValidationApi = "2.0.2"
}

object Plugins {
    const val springBoot = "org.springframework.boot"

    const val detekt = "io.gitlab.arturbosch.detekt"
    const val spotless = "com.diffplug.spotless"
    const val jacocoReportAggregation = "jacoco-report-aggregation"
    const val openApiGenerator = "org.openapi.generator"
}

object Target {
    const val jvmTarget = "17"
    val javaVersion = JavaVersion.VERSION_17
}
