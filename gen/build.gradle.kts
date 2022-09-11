plugins {
    id("java-library")
    id(Plugins.openApiGenerator).version(Versions.openApiGenerator)
}

repositories {
    mavenCentral()
}

dependencies {
    api(platform(project(":platform"))) {
        because("use common platform for dependency versions")
    }

    // needed by generated code
    implementation(Libs.Spring.bootStarterWeb) // use a more limited dep?
    implementation(Libs.swaggerAnnotations)
    implementation(Libs.javaxValidationApi)
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
            "openApiNullable" to "false" // otherwise generated code will import unwanted jackson-databind-nullable
        )
    )
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
}

openApiValidate {
    inputSpec.set("$projectDir/api.yml")
}

// Other subproject dependencies on this project need the generated classes.
configure<SourceSetContainer> {
    named("main") {
        java.srcDir("$buildDir/generated/src/main/java")
    }
}

// Ensure that code is generated for dependents. Kinda haxy since results in class files that are not used. Somehow not
// using java-library plugin might be a way out of this?
tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}
