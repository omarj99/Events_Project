# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
# Replace 'your-app-name.jar' with the actual name of your JAR file
COPY target/eventsProject-1.0.0-SNAPSHOT.jar app.jar 

# Expose the port your Spring Boot application is running on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
