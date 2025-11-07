FROM eclipse-temurin:17 as builder
WORKDIR /build

COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
COPY src ./src

RUN chmod +x mvnw && ./mvnw -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --spring.profiles.active=docker"]