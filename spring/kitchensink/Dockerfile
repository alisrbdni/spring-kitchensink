###########################################################
# 1) BUILD STAGE
#    - Uses Maven + Java 21 to compile the JAR
###########################################################
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy only the pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of your source code
COPY src/ ./src

# Compile the JAR (remove -DskipTests if you want tests)
RUN mvn clean package -DskipTests


###########################################################
# 2) RUNTIME STAGE
#    - Copies JAR into a smaller Java 21 runtime
#    - Exposes port 8080
#    - Sets the container CMD to run the JAR
###########################################################
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the final JAR from the builder
COPY --from=builder /app/target/spring-kitchensink-1.0.0-SNAPSHOT.jar /app/app.jar

# Expose port 8080 for Cloud Run
EXPOSE 8080

# Optionally install curl if you want to test *inside* the container
# RUN apk add --no-cache curl

# The main command to run when the container starts
# (Points to the JAR on port 8080)
CMD ["java", "-jar", "/app/app.jar"]


# Create a start script that:
#  1) Starts the app in the background
#  2) Waits a few seconds for startup
#  3) Sends a test POST request
#  4) If test fails, kills the app and exits
#  5) If test succeeds, continues running (attaches to the app)
# RUN echo '#!/bin/sh\n\
# set -e\n\
# echo "Starting Spring Boot application..."\n\
# java -jar /app/app.jar &\n\
# PID=$!\n\
# \n\
# echo "Waiting 10 seconds for server to start..."\n\
# sleep 10\n\
# \n\
# echo "Sending test POST request..."\n\
# if curl -f -X POST -H "Content-Type: application/json" \\\n\
#   -d '\''{\n\
#     \"name\":\"John Doe\",\n\
#     \"email\":\"john@example.com\",\n\
#     \"phoneNumber\":\"1234567890\"\n\
#   }'\'' \\\n\
#   http://localhost:8080/api/members\n\
# then\n\
#   echo "Test succeeded. Keeping container running..."\n\
#   wait $PID  # Wait on the Spring Boot process\n\
# else\n\
#   echo "Test FAILED. Stopping container..."\n\
#   kill $PID\n\
#   exit 1\n\
# fi\n' > /app/start_and_test.sh && \
#     chmod +x /app/start_and_test.sh

# # By default, run our script
# CMD ["/app/start_and_test.sh"]
