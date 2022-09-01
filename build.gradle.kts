import com.diffplug.gradle.spotless.SpotlessExtension
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
    id("com.diffplug.spotless").version("6.10.0")
}

repositories {
    mavenCentral()
}

detekt {
    source = files(rootDir)
    config = files("$rootDir/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "11"
    reports { // console output and possible failure enough for now
        xml.required.set(false)
        html.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

configure<SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        targetExclude("buildSrc/build/**/*")
        ktlint("0.46.1")
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("0.46.1")
    }
}
