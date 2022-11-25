plugins {
    id("spring-conventions")
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    implementation(project(":common"))
    implementation(project(":svc"))

    // RequestBody validation with Bean Validation
    implementation(Libs.Spring.bootStarterValidation)

    implementation("org.springframework.security:spring-security-oauth2-jose")
}
