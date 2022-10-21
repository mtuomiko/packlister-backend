plugins {
    id("spring-conventions")
    id("java-test-fixtures")
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    implementation("org.springframework:spring-context") // global configuration

    // PasswordEncoder, UserDetails & common access to user info from JWT claims
    implementation(Libs.Spring.securityOauth2ResourceServer)
    implementation(Libs.Spring.securityOauth2Jose)

    implementation(Libs.Spring.bootStarterValidation)

    implementation(Libs.jacksonModuleKotlin) // ObjectMapper configuration
}
