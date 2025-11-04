# Stage 1: Build with Maven
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with OpenJDK 21
FROM openjdk:21-slim
WORKDIR /app

RUN apt-get update && apt-get install -y curl bash && rm -rf /var/lib/apt/lists/*

RUN curl -sSL https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -o /wait-for-it.sh \
    && chmod +x /wait-for-it.sh

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["/wait-for-it.sh", "kafka:9092", "--", "/wait-for-it.sh", "postgres:5432", "--", "/wait-for-it.sh", "gateway:8080", "--", "java", "-jar", "app.jar"]
