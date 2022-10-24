plugins {
    id("kotlin-conventions")

    kotlin("plugin.spring")
}

dependencies {
    implementation(Libs.Spring.bootStarterWeb)
    implementation(Libs.microutilsKotlinLogging)
    implementation(Libs.slf4jApi)

    testImplementation(Libs.Spring.bootStarterTest)
    testImplementation(Libs.mockitoKotlin)
}
