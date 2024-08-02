FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/ubuntu_app-0.0.1.jar
COPY ${JAR_FILE} ubuntu.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ubuntu.jar"]