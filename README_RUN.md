# Run microservices stack locally with Docker

1) Put each service folder (gateway, user-service, etc.) into the project root.
2) Make sure each Spring Boot project builds with `mvn -B -DskipTests package` and produces a jar in `target/`.
3) From project root run:

   docker compose up --build

4) Open browser:
    - Frontend: http://localhost:5173
    - API Gateway: http://localhost:8080

# Logs and cleanup

- To follow logs: docker compose logs -f
- To stop and remove containers: docker compose down -v

# Notes
- If you want to use different DB credentials, update `postgres` service environment and `application-docker.properties` files accordingly.
- If a Spring Boot service fails to connect to Postgres on first run, try restarting the stack; Postgres may take a few seconds to initialize.