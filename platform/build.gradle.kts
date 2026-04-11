/**
 * Constrains dependency versions used in the whole project. Dependencies can then be used without versions in the
 * subprojects. Most versions are defined through io.quarkus.platform:quarkus-bom BOM.
 */

plugins {
    `java-platform`
}

// allow getting constraints from quarkus-bom
javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("io.quarkus.platform:quarkus-bom:3.7.4"))

    constraints {
        // declare all dependency version constraints here unless the version is defined in platform BOMs

        api("io.quarkiverse.jooq:quarkus-jooq:2.0.1")




        // override mockito version due to mockito-kotlin using an older version
        // improve: ^ not actually working, figure out
//        api("${Libs.mockitoCore}:${Versions.mockito}")
//        api("${Libs.mockitoKotlin}:${Versions.mockitoKotlin}")
//
//        api("${Libs.swaggerAnnotations}:${Versions.swaggerAnnotations}")
//
//        api("${Libs.hibernateTypes}:${Versions.hibernateTypes}")
//
//        api("${Libs.microutilsKotlinLogging}:${Versions.microutilsKotlinLogging}")
    }
}
