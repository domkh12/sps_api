# Use the GraalVM JDK as the base image
FROM ghcr.io/graalvm/jdk-community:21

# Set the working directory
WORKDIR /app

# Copy the entire project into the container
COPY . /app

# Ensure the Gradle wrapper script is executable
RUN chmod +x ./gradlew

# Run the Gradle build
RUN ./gradlew clean build

# Expose the application port
EXPOSE 8080

# Set the entry point to run the built JAR file
ENTRYPOINT ["java", "-jar", "/app/build/libs/sps-api-1.0.jar"]