# --- Этап 1: Сборка React-приложения ---
FROM node:18 AS build
WORKDIR /app

# Копируем package.json и устанавливаем зависимости
COPY package.json package-lock.json ./
RUN npm install

# Копируем остальной код и собираем статику
COPY . .
RUN npm run build

# --- Этап 2: Сервировка статитки с помощью Nginx ---
FROM nginx:1.21-alpine

# Копируем собранные файлы из этапа 'build' в директорию Nginx
COPY --from=build /app/build /usr/share/nginx/html

# Nginx по умолчанию слушает порт 80
EXPOSE 80

# Запускаем Nginx
CMD ["nginx", "-g", "daemon off;"]
