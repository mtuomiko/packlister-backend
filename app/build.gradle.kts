plugins {
    id("spring-conventions")

    id(Plugins.springBoot).version(Versions.springBoot)
    id(Plugins.springdocOpenApiGradle).version(Versions.springdocOpenApiGradle)
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

    implementation(Libs.jacksonModuleKotlin)
    implementation(Libs.Spring.bootStarterOauthResourceServer)

    implementation(Libs.springdocOpenApiStarterWebMvc)
}

// don't build plain jar (only fat one used)
tasks.getByName<Jar>("jar") {
    enabled = false
}

// springdoc-openapi-gradle-plugin workaround for Spring Boot 3.0.0
// https://github.com/springdoc/springdoc-openapi-gradle-plugin/issues/79
tasks.register<DefaultTask>("bootRunMainClassName") {
}
tasks.bootRun {
    mainClass.set("net.packlister.packlister.PacklisterApplicationKt") // refer to compiled main Class with main method
}
// end workaround

openApi {
    apiDocsUrl.set("http://localhost:8080/api-docs/api-docs.yaml")
    outputFileName.set("api-specs.yaml")
    waitTimeInSeconds.set(60)
    customBootRun {
        args.set(listOf("--spring.profiles.active=local"))
    }
}
