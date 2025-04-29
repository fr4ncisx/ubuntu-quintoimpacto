FROM maven:3.9.9-amazoncorretto-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:25-slim

WORKDIR /app

COPY --from=build /app/target/*.jar ubuntu.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ubuntu.jar"]