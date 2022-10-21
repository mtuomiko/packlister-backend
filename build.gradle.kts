import com.diffplug.gradle.spotless.SpotlessExtension
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id(Plugins.detekt).version(Versions.detekt)
    id(Plugins.spotless).version(Versions.spotless)
    id(Plugins.jacocoReportAggregation)
}

repositories {
    mavenCentral()
}

dependencies {
    jacocoAggregation(project(":app")) // use app as entrypoint for aggregation
}

reporting {
    reports {
        val jacocoMerged by creating(JacocoCoverageReport::class) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}
tasks.check {
    dependsOn(tasks.named<JacocoReport>("jacocoMerged"))
}

// use all subproject classes except generated for merged report
tasks.named("jacocoMerged", JacocoReport::class).configure {
    classDirectories.setFrom(
        files(
            subprojects.map {
                it.fileTree("${it.buildDir}/classes/kotlin/main") {
                    exclude("**/generated/*")
                }
            }
        )
    )
    reports {
        csv.required.set(true)
    }
}

detekt {
    source = files(rootDir)
    config = files("$rootDir/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = Target.jvmTarget
    reports { // console output and possible failure enough for now
        xml.required.set(false)
        html.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
    exclude(
        "buildSrc/build/**/*", // compiled buildSrc code
        "*.gradle.kts", // no need for so strict checks on scripts (unused can cause issues, for example)
        "**/testFixtures/**/*" // test code
    )
}

configure<SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        targetExclude("buildSrc/build/**/*")
        ktlint(Versions.ktlint)
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("buildSrc/build/**/*")
        ktlint(Versions.ktlint)
    }
}
