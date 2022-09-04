plugins {
    id("spring-conventions")

    kotlin("plugin.jpa").version(Versions.kotlin)
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    implementation(project(":common"))

    implementation(Libs.Spring.bootStarterDataJpa)
    implementation(Libs.kotlinReflect)
    implementation(Libs.flywayCore)

    runtimeOnly(Libs.postgres)
}
