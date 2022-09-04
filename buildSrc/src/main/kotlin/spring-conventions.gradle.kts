plugins {
    id("kotlin-conventions")

    kotlin("plugin.spring")
}

dependencies {
    implementation(Libs.Spring.bootStarterWeb)

    testImplementation(Libs.Spring.bootStarterTest)
    testImplementation(Libs.mockitoKotlin)
}
