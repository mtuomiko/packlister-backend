plugins {
    id("java-library")
    id("org.openapi.generator").version("6.0.1")
}

repositories {
    mavenCentral()
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    // needed by generated code
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("javax.validation:validation-api")
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/api.yml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("net.packlister.packlister.generated.api")
    modelPackage.set("net.packlister.packlister.generated.model")
    configOptions.set(
        mapOf(
            "delegatePattern" to "true", // creates delegate interfaces to implement
            "openApiNullable" to "false" // otherwise generated code imports jackson-databind-nullable
        )
    )
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
}

openApiValidate {
    inputSpec.set("$projectDir/api.yml")
}

// Other subproject dependencies on this need the generated classes.
configure<SourceSetContainer> {
    named("main") {
        java.srcDir("$buildDir/generated/src/main/java")
    }
}

// Ensure that code is generated for dependents. Kinda haxy since results in class files that are not used. Not using
// java-library plugin might help?
tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}
