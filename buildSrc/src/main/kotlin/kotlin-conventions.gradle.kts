import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("jacoco")
}

group = "net.packlister"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

// no subproject reports, reports are created in root project by jacoco aggregation
tasks.jacocoTestReport {
    enabled = false
}

dependencies {
    implementation(Libs.kotlinStdlib)
}
