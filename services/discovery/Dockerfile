# ---------- Этап сборки ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app


# Копируем сам сервис
COPY ./services/discovery ./services/discovery
WORKDIR /app/services/discovery

# Собираем сервис
RUN mvn clean package -DskipTests

# ---------- Этап запуска ----------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/services/discovery/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
