plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // change version also in Dependencies.kt
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.6.21")
}
