# Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app
# target folder-la irunthu generate aana .jar file-ah copy panrom
COPY --from=build /app/target/*.jar app.jar
# Unga app entha port-la run aagutho atha inga kudukanum (default 8080)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]