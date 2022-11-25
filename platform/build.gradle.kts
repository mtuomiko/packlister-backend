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

    constraints {
        // declare all dependency version constraints here unless the version is defined in platform BOMs

        // override mockito version due to mockito-kotlin using an older version
        // improve: ^ not actually working, figure out
        api("${Libs.mockitoCore}:${Versions.mockito}")
        api("${Libs.mockitoKotlin}:${Versions.mockitoKotlin}")

        api("${Libs.swaggerAnnotations}:${Versions.swaggerAnnotations}")

        api("${Libs.hibernateTypes}:${Versions.hibernateTypes}")

        api("${Libs.microutilsKotlinLogging}:${Versions.microutilsKotlinLogging}")
    }
}
