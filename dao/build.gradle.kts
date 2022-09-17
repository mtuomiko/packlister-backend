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
    // JSONB type. Match Spring Boot hibernate version (types-55 works for hibernate 5.5.x or 5.6.x).
    implementation(Libs.hibernateTypes)

    runtimeOnly(Libs.postgres)

    testImplementation(testFixtures(project(":common")))

    // can be used for checking actual db statements through proxy, see also config spy.properties in app/resources
    // implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.0")
}
