plugins {
    id("spring-conventions")
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    implementation(project(":common"))
    implementation(project(":gen"))
    implementation(project(":svc"))

    implementation("org.springframework.security:spring-security-core") // PasswordEncoder
}
