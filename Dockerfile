FROM ghcr.io/graalvm/jdk-community:21 AS build

# Copy the project files to the container
COPY --chown=gradle:gradle . /home/gradle/src

# Set the working directory
WORKDIR /home/gradle/src

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Build the project
RUN ./gradlew build --no-daemon

# Use a lightweight JDK image for the final stage
FROM eclipse-temurin:17-jdk-jammy

# Copy the built JAR file from the build stage
COPY --from=build /home/gradle/src/build/libs/sps_api-1.0.jar sps_api.jar

# Command to run the application
CMD ["java", "-jar", "sps_api.jar"]

LABEL org.name="hezf"