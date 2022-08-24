import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot").version("2.7.3")
    id("io.spring.dependency-management").version("1.0.13.RELEASE")
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
    id("com.diffplug.spotless").version("6.10.0")
    kotlin("jvm").version("1.6.21")
    kotlin("plugin.spring").version("1.6.21")
    kotlin("plugin.jpa").version("1.6.21")
}

group = "net.packlister"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Detekt>().configureEach {
    reports { }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}

detekt {
    config =
        files("$projectDir/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        ktlint("0.46.1")
    }
    kotlinGradle {
        ktlint("0.46.1")
    }
}