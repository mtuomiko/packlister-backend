plugins {
    id("spring-conventions")
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    implementation(project(":common"))
    implementation(project(":dao"))

    // PasswordEncoder, JwtEncoder...
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework:spring-tx") // transaction exceptions

    implementation(Libs.beanValidationApi)
}
