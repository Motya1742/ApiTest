# Этап сборки
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Этап исполнения
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/authenticationService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]
