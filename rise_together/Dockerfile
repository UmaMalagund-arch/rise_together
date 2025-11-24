FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source and build the JAR
COPY src ./src
RUN mvn -B clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
