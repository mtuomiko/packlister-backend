# Packlister

Deployed to Railway.app at https://packlister-production.up.railway.app (still some deficiencies like no frontend :x)

## Modules

### app

Actual Spring Boot application and configuration. Produces the final jar.

### common

Common configuration, and DTO models.

### gen

Contains API specs in OpenAPI v3 format in `api.yml`. Creates API models and delegate pattern interfaces as Java code
for Spring controllers to implement.

Caveats:

* `spring` generator (possibly wider issue?) only supports a single response net.packlister.packlister.model. Working
  around this by wrapping any response in an object that can also contain errors, so we can explicitly return an error
  from the controller. Makes the API docs a bit confusing though as the successful responses will not contain errors and
  failure responses will. Though looks the issue can also be bypassed with ExceptionHandlers.

### api

API level implementation. Spring controllers and so on.

### svc

Service level logic.

### dao

Persistence level. Handles JPA entities & repos. Runs migrations with Flyway.

## Authentication and authorization

Application is configured both as a OAuth 2.0 Resource Server and as an Authorization Server at the same time. This
doesn't make much sense in practice, but I wanted to try out the relatively
new [spring-authorization-server](https://github.com/spring-projects/spring-authorization-server) which should get its
`1.0.0` major release in November 2022. Authorization server setup is fairly limited in this project, it does not use
for example any DB tables, so it's a pretty static configuration.

The authorization server is configured for a single public client, that is, the packlister frontend. The only supported
OAuth 2.0 authorization flow is the authorization code flow with Proof Key for Code Exchange (PKCE). JWT signing uses
RS256 based on private key. See [Environment variables](#environment-variables).

Using long-life access tokens since Spring Authorization Server does not allow refresh tokens for public clients due to
inherent issues with securing them in the browser. Note, this is not a good idea in practice! It will have to suffice
for now for this POC.

Authorization server endpoints should be available through `/.well-known/oauth-authorization-server` endpoint. They are
not listed in the API yaml for generated code.

## Development

App assumes an existing PostgreSQL database to be available at `postgresql://localhost:5432/packlister` with
credentials `postgres:Hunter2` when running with `local` profile.

Run one with docker using for
example `docker run -d --restart unless-stopped --name dev-postgres -p 5432:5432 -e POSTGRES_DB=packlister -e POSTGRES_PASSWORD=Hunter2 postgres:14`

Run locally using Gradle task `bootRun` using `local` profile, for example by providing env
var `SPRING_PROFILES_ACTIVE=local`.

## Environment variables

| Variable                                    | Description                                                                                              | Default value | Required | Example                                                               |
|---------------------------------------------|----------------------------------------------------------------------------------------------------------|---------------|----------|-----------------------------------------------------------------------|
| PORT                                        | Application port                                                                                         | `8080`        |          |                                                                       |
| DATABASE_CONNECTION_URL                     | JDBC connection URL                                                                                      |               | ✓        | `jdbc:postgresql://host.docker.internal:5432/packlister`              |
| DATABASE_CONNECTION_USERNAME                | DB username                                                                                              |               | ✓        | `postgres`                                                            |
| DATABASE_CONNECTION_PASSWORD                | DB password                                                                                              |               | ✓        | `Hunter2`                                                             |
| DATABASE_SCHEMA                             | DB schema                                                                                                | `packlister`  |          |                                                                       |
| PACKLISTER_AUTHSERVER_CLIENT_ID             | Identifier for the single supported public client of the authorization server                            |               | ✓        | `packlister-client    `                                               |
| PACKLISTER_AUTHSERVER_ISSUER                | Authorization server issuer, an URI                                                                      |               | ✓        | `http://localhost:8080`                                               |
| PACKLISTER_AUTHSERVER_ALLOWED_REDIRECT_URIS | Comma separated list of allowed redirect uris for authorization server used in `authorization_code` flow |               |          | `http://localhost:3003/authorized,https://oidcdebugger.com/debug`     |
| PACKLISTER_AUTHSERVER_RSA_PRIVATE_KEY       | PEM format RSA private key. Allows newline escaping. Example uses escaped newlines.                      |               | ✓        | `-----BEGIN PRIVATE KEY-----\nSTUFF\nHERE\n-----END PRIVATE KEY-----` |

RSA key can be generated using, for example, `openssl genrsa -out key.pem 2048`. Newlines can be kept as is or escaped
by converting them to `\n`.

### Containerized

File `.env.dev` has an example of needed env vars for running the image. Database host (example
used `host.docker.internal`) might vary depending on setup.

Example commands

* build image `docker build -t packlister .`
* run `docker run --rm -it -p 8080:8080 --env-file ./.env.dev packlister`
