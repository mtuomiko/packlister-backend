# Smallish Java 11 JRE. Different Java image options seem confusing so not sure of possible caveats
FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine

# don't run as root
RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /app \
    && chown -R spring:spring /app
USER spring:spring

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
