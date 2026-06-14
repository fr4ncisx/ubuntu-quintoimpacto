# ---------- STAGE 1: Deps
FROM maven:3.9.11-eclipse-temurin-21-alpine AS deps

WORKDIR /workspace

COPY pom.xml ./

RUN --mount=type=cache,id=maven,target=/root/.m2 \
    mvn -B -q -ntp -DskipTests dependency:go-offline


# ---------- STAGE 2: Build
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build

WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN --mount=type=cache,id=maven,target=/root/.m2 \
    mvn -B -q -ntp clean package -DskipTests && \
    find target -maxdepth 1 -type f -name "*.jar" ! -name "*.jar.original" -exec mv {} /workspace/app.jar \; && \
    test -f /workspace/app.jar


# ---------- STAGE 3: JRE
FROM eclipse-temurin:21-alpine-3.23 AS jre-builder

RUN jlink \
    --add-modules java.base,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,java.xml,java.compiler,java.net.http,jdk.unsupported,jdk.crypto.ec,jdk.zipfs \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=zip-9 \
    --output /custom-jre


# ---------- STAGE 4: Runtime
FROM alpine:3.24

LABEL maintainer="semilleroubuntu.dev@gmail.com" \
      org.opencontainers.image.source="https://github.com/fr4ncisx/ubuntu-quintoimpacto" \
      org.opencontainers.image.title="ubuntu_app" \
      org.opencontainers.image.description="Spring Boot App para Quinto Impacto" \
      org.opencontainers.image.licenses="Apache-2.0"

ENV APP_HOME=/app \
    SERVER_PORT=8080 \
    JAVA_HOME=/opt/java/openjdk \
    PATH="/opt/java/openjdk/bin:${PATH}" \
    JAVA_TOOL_OPTIONS="-Xms64m -Xmx220m -XX:+UseContainerSupport -XX:+UseSerialGC -XX:MaxMetaspaceSize=128m -XX:ReservedCodeCacheSize=48m -Xss512k -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true" \
    SERVER_TOMCAT_THREADS_MAX=20 \
    SERVER_TOMCAT_THREADS_MIN_SPARE=2

WORKDIR /app

RUN apk add --no-cache libstdc++ ca-certificates tzdata && \
    addgroup -S -g 10001 spring && \
    adduser -S -D -H -u 10001 -G spring -s /sbin/nologin spring

COPY --from=jre-builder /custom-jre /opt/java/openjdk

COPY --from=build --chown=spring:spring --chmod=0444 /workspace/app.jar /app/app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]