FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем общие модули
COPY ./common ./common

# Устанавливаем общие артефакты в локальный .m2
RUN mvn -f common/kafka/pom.xml clean install -DskipTests
RUN mvn -f common/apiclient/pom.xml clean install -DskipTests

# Копируем сам сервис
COPY ./services/apartment_api ./services/apartment_api
WORKDIR /app/services/apartment_api

# Собираем сервис
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/services/apartment_api/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
