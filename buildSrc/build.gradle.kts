plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // change version also in buildSrc/src/main/kotlin/Dependencies.kt
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.7.20")
}
