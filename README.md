# Packlister

Deployed to Railway.app at https://packlister-production.up.railway.app (still some deficiencies like no frontend :x)

## Modules

### app

Actual Spring Boot application and configuration. Produces the final jar.

### common

Common configuration, and DTO models.

### gen

Contains API specs in OpenAPI v3 format in `api.yml`. Creates API models and delegate pattern interfaces as Java code for Spring controllers to implement.

Caveats:
* `spring` generator (possibly wider issue?) only supports a single response net.packlister.packlister.model. Working around this by wrapping any response in an object that can also contain errors, so we can explicitly return an error from the controller. Makes the API docs a bit confusing though as the successful responses will not contain errors and failure responses will. Though looks the issue can also be bypassed with ExceptionHandlers.

### api

API level implementation. Spring controllers and so on.

### svc

Service level logic.

### dao

Persistence level. Handles JPA entities & repos. Runs migrations with Flyway.

## Authentication and authorization

Simple JWT based solution. Doesn't use any "real" authorization server for issuing tokens. [spring-authorization-server](https://github.com/spring-projects/spring-authorization-server) seemed like an interesting option but first major release is still in the future when writing this.

Application is configured as a Spring OAuth 2.0 Resource Server consuming tokens issued by the application itself with HS256 algorithm using a single "shared" secret key.

## Development

App assumes an existing PostgreSQL database to be available at `postgresql://localhost:5432/packlister` with credentials `postgres:Hunter2`

Run one with docker using for example `docker run -d --restart unless-stopped --name dev-postgres -p 5432:5432 -e POSTGRES_DB=packlister -e POSTGRES_PASSWORD=Hunter2 postgres:14`

Run locally using Gradle task `bootRun` using `local` profile, for example by providing env var `SPRING_PROFILES_ACTIVE=local`.

### Containerized

File `.env.dev` has an example of needed env vars for running the image. Database host (example used `host.docker.internal`) might vary depending on setup.

Example commands
* build image `docker build -t packlister .`
* run `docker run --rm -it -p 8080:8080 --env-file ./.env.dev packlister`
