# Развертывание зависимостей с помощью Helm

Для развертывания `PostgreSQL`, `Kafka` и `Zookeeper` мы будем использовать Helm.
Helm — это менеджер пакетов для Kubernetes, который упрощает развертывание и управление приложениями.

## 1. Установка Helm

Если у вас еще не установлен Helm, следуйте [официальной инструкции](https://helm.sh/docs/intro/install/).

## 2. Развертывание PostgreSQL

Мы будем использовать чарт от Bitnami.

```bash
# Добавляем репозиторий Bitnami
helm repo add bitnami https://charts.bitnami.com/bitnami

# Устанавливаем PostgreSQL
helm install postgres bitnami/postgresql \
  --set auth.postgresPassword=root \
  --set auth.database=sorokindb \
  --set fullnameOverride=postgres
```

**Важные замечания:**
*   `fullnameOverride=postgres`: это создаст сервис с именем `postgres`, на который ссылаются ваши Java-приложения.
*   `auth.postgresPassword=root`: устанавливает пароль пользователя `postgres`.
*   `auth.database=sorokindb`: создает базу данных `sorokindb`.

После установки, ваши приложения смогут подключиться к PostgreSQL по адресу `postgres:5432`.

## 3. Развертывание Kafka и Zookeeper

Мы будем использовать чарт от Confluent.

```bash
# Добавляем репозиторий Confluent
helm repo add confluentinc https://confluentinc.github.io/cp-helm-charts/

# Устанавливаем Kafka
helm install kafka confluentinc/cp-helm-charts \
    --set cp-zookeeper.enabled=true \
    --set cp-kafka.brokers.count=1 \
    --set cp-schema-registry.enabled=false \
    --set cp-kafka-rest.enabled=false \
    --set cp-kafka-connect.enabled=false \
    --set cp-ksql-server.enabled=false \
    --set fullnameOverride=kafka
```

**Важные замечания:**
*   `fullnameOverride=kafka`: это создаст сервис Kafka с именем `kafka`, на который ссылаются ваши Java-приложения.
*   Мы отключаем дополнительные компоненты Confluent (`schema-registry`, `kafka-rest` и т.д.), так как они не требуются для вашего приложения.
*   Этот чарт автоматически развернет Zookeeper как зависимость.

После установки, ваши приложения смогут подключиться к Kafka по адресу `kafka:9092`.
