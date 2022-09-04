plugins {
    id("spring-conventions")

    id(Plugins.springBoot).version(Versions.springBoot)
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    implementation(project(":api"))

    implementation(Libs.Spring.bootStarterWeb)
    implementation(Libs.Spring.bootStarterDataJpa)
    implementation(Libs.Spring.bootStarterActuator)
    implementation(Libs.jacksonModuleKotlin)
//    implementation(Libs.flywayCore)

//    runtimeOnly(Libs.postgres)
}

// don't build plain jar (only fat one used)
tasks.getByName<Jar>("jar") {
    enabled = false
}
