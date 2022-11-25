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
    implementation("${Libs.javaxValidationApi}:${Versions.javaxValidationApi}")
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
