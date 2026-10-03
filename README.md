# FIAP X Video API

Spring Boot 3.5 / Java 21. Owns authentication, video metadata, status and the transactional outbox. The [central architecture and startup guide](https://github.com/CarlosDanyel/INFRA-Tech-Challenge-Fase-5) is in the infra repository.

## API

OpenAPI: `/v3/api-docs`; Swagger UI: `/swagger-ui/index.html`. Import [`postman/fiapx.postman_collection.json`](postman/fiapx.postman_collection.json) into Postman. All video routes require `Authorization: Bearer <token>`.

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/api/auth/register` | Create user and return JWT |
| POST | `/api/auth/login` | Return JWT |
| POST | `/api/videos` | Multipart field `video`; return 202 |
| GET | `/api/videos` | List current user's videos |
| GET | `/api/videos/{id}` | Get current user's video |
| GET | `/api/videos/{id}/download` | Download completed ZIP |
| POST | `/api/videos/{id}/retry` | Retry a failed video |

Upload accepts MP4, AVI, MOV and MKV up to 250 MB. The processor validates media with FFmpeg. JWT expires after 24 hours. Password hashes use BCrypt. `created_at` and `updated_at` are stored for users, videos and outbox events.

Java sources and tests live under `src/main/java/Tech_Challenge_Fase_5/video_api_Tech_Challenge_Fase_5` and `src/test/java/Tech_Challenge_Fase_5/video_api_Tech_Challenge_Fase_5`. The package root is `Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5`.

## Run and test

Copy `.env.example` to `.env`, configure a random `JWT_SECRET` of at least 32 bytes, and start dependencies from the infra repository. Export the variables with `set -a; source .env; set +a`, set `DB_NAME=fiapx`, `DB_PORT=55433` and `SERVER_PORT=18080`, then run `JAVA_HOME=$(/usr/libexec/java_home -v 21) ./gradlew bootRun`. Run tests with `./gradlew clean test`. Flyway creates the schema from [`V1__initial.sql`](src/main/resources/db/migration/V1__initial.sql).

The release branch runs tests and publishes a GHCR image. A PR to master must come from release. Master additionally deploys when `DEPLOY_ENABLED=true` and `KUBE_CONFIG_B64` are configured.
