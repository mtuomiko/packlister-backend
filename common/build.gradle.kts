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
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    implementation(Libs.jacksonModuleKotlin) // ObjectMapper configuration
}
