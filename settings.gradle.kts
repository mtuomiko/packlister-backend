rootProject.name = "packlister"

include(
    "platform",
    "app",
    "common",
    "gen",
    "api",
    "svc",
    "dao"
)

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
