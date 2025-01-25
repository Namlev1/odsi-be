# Use an official OpenJDK 21 runtime as the base image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Copy the source code
COPY src /app/src

# Build the application (skip tests)
RUN ./gradlew build --no-daemon -x test

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "build/libs/be-0.0.1-SNAPSHOT.jar"]