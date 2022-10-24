/**
 * Constrains dependency versions used in the whole project. Dependencies can then be used without versions in the
 * subprojects. Most versions are defined through spring-boot-dependencies BOM.
 */

plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:${Versions.springBoot}"))

    // declare all dependency version constraints here unless defined in platform BOMs
    constraints {
        // override mockito version due to mockito-kotlin using an older version
        // improve: ^ not actually working, figure out
        api("${Libs.mockitoCore}:${Versions.mockito}")
        api("${Libs.mockitoKotlin}:${Versions.mockito}")

        api("${Libs.swaggerAnnotations}:${Versions.swaggerAnnotations}")

        api("${Libs.hibernateTypes}:${Versions.hibernateTypes}")

        api("${Libs.microutilsKotlinLogging}:${Versions.microutilsKotlinLogging}")
    }
}
