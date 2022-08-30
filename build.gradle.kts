import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    id("com.diffplug.spotless").version("6.10.0")
}

repositories {
    mavenCentral()
}

configure<SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        ktlint("0.46.1")
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("0.46.1")
    }
}
