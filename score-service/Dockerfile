# Stage 1: build with Maven (uses mvnw if present)
FROM eclipse-temurin:17 as builder
WORKDIR /build

# copy mvnw and wrapper files if present (keeps cache)
COPY mvnw ./
COPY .mvn .mvn
# copy pom and source
COPY pom.xml ./
COPY src ./src

# make mvnw executable (if present) and build
RUN if [ -f mvnw ]; then chmod +x mvnw && ./mvnw -B -DskipTests package; else mvn -B -DskipTests package; fi

# Stage 2: runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# copy artifact: find any jar in target/
COPY --from=builder /build/target/*.jar app.jar

# Expose port (services define their own server.port in application-docker.properties)
# run with docker profile active
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --spring.profiles.active=docker"]