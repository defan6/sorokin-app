# Стадия 1: Сборка приложения с помощью Maven
FROM maven:3.9-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем родительский pom.xml
COPY pom.xml .

# Копируем pom.xml и исходный код сервиса event-notificator
COPY event-notificator/pom.xml ./event-notificator/
COPY event-notificator/src ./event-notificator/src/

# Собираем только модуль event-notificator (флаг -am соберет и все его зависимости)
RUN mvn -pl event-notificator -am clean package -DskipTests

# Стадия 2: Создание финального минималистичного образа
FROM openjdk:21-jdk-slim

WORKDIR /app

# Копируем собранный JAR-файл из стадии сборщика
COPY --from=builder /app/event-notificator/target/*.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java","-jar","/app/app.jar"]