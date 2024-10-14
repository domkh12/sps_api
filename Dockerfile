# PHASE 1 - Download & Install JDK

FROM ghcr.io/graalvm/jdk-community:21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Build the project
RUN ./gradlew build --no-daemon

LABEL org.name="hezf"

FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /home/gradle/src/build/libs/sps_api-1.0.jar sps_api.jar
#WORKDIR app
#ADD ./build/libs/sps-api-1.0.jar /app/
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/sps-api-1.0.jar"]
ENTRYPOINT ["java","-jar","/app.jar"]