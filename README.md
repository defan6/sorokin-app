# Sorokin Microservices Project

## Описание

Микросервисная система для управления событиями и пользователями, построенная на Spring Boot, Kafka и PostgreSQL. Система предназначена для регистрации пользователей, создания событий, бронирования участия и отправки уведомлений.

---

## Архитектура проекта

```
[client]
   |
   v
[gateway] ---> [auth]
   |             |
   |             v
   |         [event-manager] <----> [event-notificator]
   |___________________________^
         (Kafka)
```

- **gateway** — API Gateway, маршрутизация, JWT-фильтрация.
- **auth** — сервис аутентификации, регистрации, управления пользователями.
- **event-manager** — сервис управления событиями, бронированиями, местами.
- **event-notificator** — сервис отправки уведомлений на основе событий из Kafka.
- **PostgreSQL** — единая база данных для хранения пользователей, событий, бронирований, ролей, мест.
- **Kafka + Zookeeper** — асинхронный обмен событиями между сервисами.

---

## Сервисы

### 1. Auth

- Регистрация и аутентификация пользователей.
- Хранение пользователей и их ролей.
- Генерация и валидация JWT.

**Основные таблицы:**
- `auths` (id, username, full_name, password)
- `auth_roles` (auth_id, roles)

### 2. Gateway

- API Gateway на Spring Cloud Gateway.
- JWT-фильтрация, маршрутизация запросов к сервисам.

### 3. Event Manager

- CRUD для событий, мест, бронирований.
- Управление пользователями и ролями.
- Отправка событий в Kafka.

**Основные таблицы:**
- `users` (id, username, password, full_name, created_at)
- `roles` (id, role)
- `users_roles` (user_id, role_id)
- `venues` (id, name, address, capacity)
- `events` (id, title, description, event_date, venue_id, organizer_id)
- `bookings` (id, user_id, event_id, booking_status, registered_at)

### 4. Event Notificator

- Получение событий из Kafka.
- Отправка уведомлений пользователям.

**Основная таблица:**
- `notifications` (id, username, event_id, message, booking_status, read_status, registered_at)

---

## Схемы БД

### Auth

```sql
CREATE TABLE auths (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE auth_roles (
    auth_id BIGINT NOT NULL,
    roles VARCHAR(255) NOT NULL,
    CONSTRAINT fk_auth FOREIGN KEY(auth_id) REFERENCES auths(id)
);
```

### Event Manager

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE venues (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    capacity BIGINT NOT NULL
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date TIMESTAMP NOT NULL,
    venue_id BIGINT NOT NULL,
    organizer_id BIGINT NOT NULL,
    CONSTRAINT fk_event_venue FOREIGN KEY (venue_id) REFERENCES venues (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_organizer FOREIGN KEY (organizer_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    booking_status VARCHAR(50) NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_event FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);
```

### Event Notificator

```sql
CREATE TABLE notifications (
    id bigserial primary key,
    username varchar(255) not null,
    event_id bigint not null,
    message varchar(255) null,
    booking_status varchar(50) not null,
    read_status varchar(50) not null,
    registered_at timestamp default current_timestamp
);
```

---

## Как запустить проект

1. Установите Docker и Docker Compose.
2. Клонируйте репозиторий.
3. В корне выполните:

```bash
docker-compose up --build
```

4. Сервисы будут доступны на портах:
   - gateway: 8080
   - auth: 8081
   - event-manager: 8082
   - event-notificator: 8083
   - postgres: 5433
   - kafka: 9092

5. Для работы с API используйте Postman/Swagger (Swagger рекомендуется добавить).

---

## Переменные окружения

- POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB — для БД
- JWT_SECRET — для auth/gateway

---

## TODO и рекомендации

- Добавить Swagger/OpenAPI для всех REST API
- Описать топики Kafka и схемы сообщений
- Внедрить централизованное логирование и мониторинг
- Покрыть код тестами
- Выровнять версии Spring Boot и зависимостей
- Добавить CI/CD pipeline
