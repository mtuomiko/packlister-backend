/**
 * This platform is meant to unify all dependency versions used in the whole project. Direct dependency versions
 * should not be used outside this.
 */

plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:2.7.3"))

    // declare all dependency version constraints here unless defined in platform BOMs
    constraints {
        api("io.swagger.core.v3:swagger-annotations:2.2.2")
        api("javax.validation:validation-api:2.0.1.Final")
        api("org.assertj:assertj-core:3.11.1")
    }
}
