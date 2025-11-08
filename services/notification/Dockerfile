# ---------- Этап сборки ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем общие модули
COPY ./common/kafka ./common/kafka
COPY ./common/apiclient ./common/apiclient

# Устанавливаем их в локальный .m2 внутри контейнера
RUN mvn -f common/kafka/pom.xml clean install -DskipTests
RUN mvn -f common/apiclient/pom.xml clean install -DskipTests

# Копируем сам сервис
COPY ./services/notification ./services/notification
WORKDIR /app/services/notification

# Собираем сервис
RUN mvn clean package -DskipTests

# ---------- Этап запуска ----------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/services/notification/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

