# Используем надежный базовый образ с Java Runtime
FROM eclipse-temurin:21-jre

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем ТОЛЬКО готовый JAR-файл, который был собран ранее в пайплайне
COPY target/*.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java","-jar","/app/app.jar"]
