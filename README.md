# Packlister

And so on...

### Development

App assumes an existing database to be available at `jdbc:postgresql://localhost:5432/packlister` with credentials `postgres:Hunter2`

#### Containerized

File `.env.dev` has an example of needed env vars for running the image. Database host (example used `host.docker.internal`) might vary depending on setup.

Example commands
* build `docker build -t packlister .`
* run `docker run -p 8080:8080 --env-file ./.env.dev packlister`
