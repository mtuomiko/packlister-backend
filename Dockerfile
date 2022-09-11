# Smallish Java 17 JRE. Different available Java image options seem confusing so not sure of possible caveats
FROM eclipse-temurin:17.0.4.1_1-jre-alpine

# don't run as root
RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /app \
    && chown -R spring:spring /app
USER spring:spring

WORKDIR /app

COPY app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
