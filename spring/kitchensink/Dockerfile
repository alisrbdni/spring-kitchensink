# Use Eclipse Temurin’s Java 21 JDK on Alpine Linux
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built JAR file into the container
COPY target/spring-kitchensink-1.0.0-SNAPSHOT.jar /app/app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Define the startup command
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
