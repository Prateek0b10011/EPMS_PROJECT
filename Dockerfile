# Use a lightweight Java image
FROM eclipse-temurin:25-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file you built locally into the container
COPY app.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
