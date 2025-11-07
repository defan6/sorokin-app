# Stage 1: Build the entire multi-module project
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the final image for the event-manager service
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

RUN apt-get update && apt-get install -y curl bash && rm -rf /var/lib/apt/lists/*

RUN curl -sSL https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -o /wait-for-it.sh \
    && chmod +x /wait-for-it.sh

# Copy only the required JAR from the build stage
COPY --from=build /app/event-manager/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["/wait-for-it.sh", "kafka:9092", "--", "/wait-for-it.sh", "postgres:5432", "--", "/wait-for-it.sh", "gateway:8080", "--", "java", "-jar", "app.jar"]
