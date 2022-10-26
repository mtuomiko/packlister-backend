# Packlister

This is the backend service repository for Packlister, a hobby project aiming to provide a similar service
as [LighterPack](https://lighterpack.com/).

**Note! Application backend is deployed on a free plan at render.com which has very conservative resource limits and
also suspends inactive services. Starting a new instance of the backend can take up to 5 minutes!**

The application frontend is deployed at https://packlister.onrender.com with a separate backend (this project) deployed
to `packlister-svc.onrender.com`. Still work in progress...

## Modules

The application has been split to subprojects / modules in an effort to promote separation of concerns and enforce
sensible dependencies between the modules.

### app

Actual Spring Boot application and configuration. Produces the final fat JAR file.

### common

Common configuration, and DTO models. Available to all other (application) modules.

### gen

Contains API specs in OpenAPI v3 format in [api.yml](/gen/api.yml). Creates API models and delegate pattern interfaces
as Java code for the Spring controllers to implement.

Caveats:

`spring generator` for OpenAPI only supports a single response model which limits the options for error situations.
Direct return from the Spring controller in case of a failure would require that the error model is an optional part of
the single response model, which gets a bit messy. Currently working around the issue by using ExceptionHandlers meaning
all failures are handled through Exceptions since that allows us to bypass the response model for a successful
operation.

### api

API level implementation. Spring controllers and so on.

### svc

Service level logic.

### dao

Persistence level. Handles JPA entities and repositories. Runs migrations with Flyway. Migrations stored
at `dao/src/main/resources/migrations/`.

### buildSrc

Gradle Kotlin source code used for building the application. Contains names and versions (unless coming from Spring
BOMs) for libraries and plugins. Also has common module gradle configurations (conventions).

### platform

Java platform subproject (see
[The Java Platform Gradle Plugin](https://docs.gradle.org/current/userguide/java_platform_plugin.html)) enforcing
dependency versions project-wide. All dependency **versions** should be declared here, not directly in the subproject
where they are used. Most of the dependency versions come directly
from [Spring Boot Dependencies BOM](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies)
and do not need to be manually handled.

## Authentication and authorization

*Disclaimer: I realize that this configuration probably does not make sense for any real world project. More sensible
alternative on this scale would probably be to just use Spring sessions with internal user DB. External alternatives
could be for example AWS Cognito or Auth0, or running Keycloak or Spring Authorization Server separately.*

Application is configured as a Spring OAuth 2.0 Resource Server consuming self issued JWTs. I tried out the
newish [spring-authorization-server](https://github.com/spring-projects/spring-authorization-server) (which should get
its `1.0.0` major release in November 2022) but it didn't really make any practical sense for this project. Configuring
the application both as a fully-capable Authorization Server and also a Resource Server at the same time was just goofy.

The setup uses self issued HS256 signed bearer tokens with the access tokens used "normally" on a request HTTP header
and the refresh tokens set as a `HttpOnly` cookie on the auth paths.

Refresh tokens are rotated automatically and their reuse will invalidate any refresh token originating from the same
login. The original refresh token created using user credentials (username, password) is issued a random family UUID
that is retained by all subsequent refresh tokens. If refresh token reuse is detected, then that family is no longer
valid forcing all actors to re-authenticate. Access tokens are not revoked.

## Environment variables

| Variable                         | Description                                                                                                                                 | Default value | Required | Example                                                        |
|----------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|---------------|----------|----------------------------------------------------------------|
| `PORT`                           | Application port                                                                                                                            | `8080`        |          |                                                                |
| `DATABASE_CONNECTION_URL`        | JDBC connection URL                                                                                                                         |               | ✓        | `jdbc:postgresql://host.docker.internal:5432/packlister`       |
| `DATABASE_CONNECTION_USERNAME`   | DB username                                                                                                                                 |               | ✓        | `postgres`                                                     |
| `DATABASE_CONNECTION_PASSWORD`   | DB password                                                                                                                                 |               | ✓        | `Hunter2`                                                      |
| `DATABASE_SCHEMA`                | DB schema                                                                                                                                   | `packlister`  |          |                                                                |
| `PACKLISTER_AUTH_DOMAIN`         | Domain setting for refresh token cookies                                                                                                    |               | ✓        | `localhost`, `packlister-svc.onrender.com`                     |
| `PACKLISTER_AUTH_ISSUER`         | JWT token issuer. Does not necessarily need to be the server URL, but following OIDC specs here (not that they really matter in this case). |               | ✓        | `http://localhost:8080`, `https://packlister-svc.onrender.com` |
| `PACKLISTER_AUTH_ALLOWED_ORIGIN` | Single allowed origin for CORS, set as the frontend URL                                                                                     |               | ✓        | `http://localhost:3003`, `https://packlister.onrender.com`     |
| `PACKLISTER_AUTH_JWT_SECRET`     | Base64 encoded secret key for JWT signing. Must be 512 bits (64 bytes) in size (before encoding).                                           |               | ✓        |                                                                |

Base64 encoded 64 byte secret key can be generated, for
example, using `dd if=/dev/urandom bs=64 count=1 2>/dev/null | base64`.

## Development

App assumes an existing PostgreSQL database to be available at `postgresql://localhost:5432/packlister` with
credentials `postgres:Hunter2` when running with `local` profile.

Run one for example with docker
using `docker run -d --restart unless-stopped --name dev-postgres -p 5432:5432 -e POSTGRES_DB=packlister -e POSTGRES_PASSWORD=Hunter2 postgres:14`

Run application locally using Gradle task `bootRun` using `local` profile, for example by providing env
var `SPRING_PROFILES_ACTIVE=local`.

### Containerized

The image is built using [`Dockerfile`](Dockerfile). [jib](https://github.com/GoogleContainerTools/jib) would probably
make a lot more sense for building the image but render.com deployment forces our hand.

File [`.env.dev`](.env.dev) has an example of needed env vars for running the image. Database host (example
uses `host.docker.internal`) might vary depending on your setup.

Example commands using docker

* build image `docker build -t packlister .` (remember to build the fat JAR beforehand)
* run `docker run --rm -it -p 8080:8080 --env-file ./.env.dev packlister`
