plugins {
    id("spring-conventions")

    id(Plugins.springBoot).version(Versions.springBoot)
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    implementation(project(":common"))
    implementation(project(":api"))

    implementation(Libs.Spring.bootStarterWeb)
    implementation(Libs.Spring.bootStarterSecurity)
    implementation(Libs.Spring.bootStarterDataJpa)
    implementation(Libs.Spring.bootStarterActuator)
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(Libs.jacksonModuleKotlin)
    implementation(Libs.Spring.bootStarterOauthResourceServer)
    implementation(Libs.Spring.authorizationServer)
    implementation("org.bouncycastle:bcpkix-jdk18on:1.72")
}

// don't build plain jar (only fat one used)
tasks.getByName<Jar>("jar") {
    enabled = false
}
