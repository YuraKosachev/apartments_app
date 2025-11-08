# ---------- Этап сборки ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app


# Копируем сам сервис
COPY ./services/configuration ./services/configuration
WORKDIR /app/services/configuration

# Собираем сервис
RUN mvn clean package -DskipTests

# ---------- Этап запуска ----------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/services/configuration/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]