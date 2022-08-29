import com.diffplug.gradle.spotless.SpotlessExtension
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot").version("2.7.3")
    id("io.spring.dependency-management").version("1.0.13.RELEASE")
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
    id("com.diffplug.spotless").version("6.10.0")
    id("org.openapi.generator").version("6.0.1")
    kotlin("jvm").version("1.6.21")
    kotlin("plugin.spring").version("1.6.21")
    kotlin("plugin.jpa").version("1.6.21")
}

group = "net.packlister"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // needed by generated code
    implementation("io.swagger.core.v3:swagger-annotations:2.2.2")
    implementation("javax.validation:validation-api:2.0.1.Final")

    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// don't build plain jar (only fat one used)
tasks.getByName<Jar>("jar") {
    enabled = false
}

// don't create reports, failure with console output enough for now
tasks.withType<Detekt>().configureEach {
    reports { }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}

// application code needs generated code
tasks.compileKotlin {
    dependsOn(tasks.openApiGenerate)
}

detekt {
    config =
        files("$projectDir/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
}

configure<SpotlessExtension> {
    kotlin {
        ktlint("0.46.1")
    }
    kotlinGradle {
        ktlint("0.46.1")
    }
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/specs/api.yml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("net.packlister.packlister.generated.api")
    modelPackage.set("net.packlister.packlister.generated.model")
    configOptions.set(
        mapOf(
            "delegatePattern" to "true", // creates delegate interfaces to implement
            "openApiNullable" to "false" // otherwise generated code imports jackson-databind-nullable
        )
    )
    ignoreFileOverride.set("$rootDir/.openapi-generator-ignore")
}

openApiValidate {
    inputSpec.set("$rootDir/specs/api.yml")
}

// include generated code
configure<SourceSetContainer> {
    named("main") {
        java.srcDir("$buildDir/generated/src/main/java")
    }
}
