# ---------- STAGE 1: Build (compilación del JAR)
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn verify -DskipTests -B

# ---------- STAGE 2: Runtime (producción)
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="semilleroubuntu.dev@gmail.com"
LABEL org.opencontainers.image.source="https://github.com/fr4ncisx/ubuntu-quintoimpacto"
LABEL org.opencontainers.image.title="ubuntu_app"
LABEL org.opencontainers.image.description="Spring Boot App para Quinto Impacto"
LABEL org.opencontainers.image.licenses="Apache License"

RUN addgroup -S spring && adduser -S -G spring spring

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN chown spring:spring app.jar && chmod 644 app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseContainerSupport", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "app.jar"]