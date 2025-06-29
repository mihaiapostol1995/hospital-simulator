# Use an official OpenJDK runtime as a base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and project files
COPY . ./

# Build the application
RUN ./mvnw dependency:resolve
RUN ./mvnw package -DskipTests

# Copy the built JAR file
COPY target/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]