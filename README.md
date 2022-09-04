# Packlister

Deployed to https://packlister.fly.dev (no functionality yet).

## Development

App assumes an existing PostgreSQL database to be available at `postgresql://localhost:5432/packlister` with credentials `postgres:Hunter2`

Run one with docker using for example `docker run -d --restart unless-stopped --name dev-postgres -p 5432:5432 -e POSTGRES_DB=packlister -e POSTGRES_PASSWORD=Hunter2 postgres:14`

### Containerized

File `.env.dev` has an example of needed env vars for running the image. Database host (example used `host.docker.internal`) might vary depending on setup.

Example commands
* build image `docker build -t packlister .`
* run `docker run -it -p 8080:8080 --env-file ./.env.dev packlister`

### Fly.io (mainly note to self)

Proxy access to production DB can be given with `flyctl proxy 15432:5432 -a packlister-db`

## Modules

### app

Actual Spring Boot application and configuration. Produces the final jar.

### common

Common DTO models used between api<->svc<->dao.

### gen

Contains API specs in OpenAPI v3 format in `api.yml`. Creates API models and delegate pattern interfaces as Java code for Spring controllers to implement.

Caveats:
* `spring` generator (possibly wider issue?) only supports a single response model. Working around this by wrapping any response in an object that can also contain errors, so we can explicitly return an error from the controller. Makes the API docs a bit confusing though as the successful responses will not contain errors and failure responses will. Though looks the issue can also be bypassed with ExceptionHandlers.

### api

API level implementation. Spring controllers and so on.

### svc

Service level logic.

### dao

Persistence level. Handles JPA entities & repos. Runs migrations with Flyway.
