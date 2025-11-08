FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем общие модули
COPY ./common ./common

# Устанавливаем общие артефакты в локальный .m2
RUN mvn -f common/kafka/pom.xml clean install -DskipTests
RUN mvn -f common/apiclient/pom.xml clean install -DskipTests

# Копируем сам сервис
COPY ./services/data_provider ./services/data_provider
WORKDIR /app/services/data_provider

# Собираем сервис
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/services/data_provider/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

