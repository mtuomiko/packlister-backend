# Don't care as much of the specific build image
FROM eclipse-temurin:17-jdk as build-stage

WORKDIR /build

COPY . .

# Intent is to just build the image. Any checks for correctness or quality should be done elsewhere.
# Exclude tests due to them needing a DB, exclude check because of possible line endings fails on static checks
RUN ./gradlew clean build -x test -x check

# Smallish Java 17 JRE. Different available Java image options seem confusing so not sure of possible caveats
FROM eclipse-temurin:17.0.4.1_1-jre-alpine

# Don't run as root
RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /app \
    && chown -R spring:spring /app
USER spring:spring

WORKDIR /app

COPY --from=build-stage /build/app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
