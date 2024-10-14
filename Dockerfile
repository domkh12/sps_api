# PHASE 1 - Download & Install JDK

FROM gradle:jdk17-jammy AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

LABEL org.name="hezf"

FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /home/gradle/src/build/libs/sps_api-1.0.jar sps_api.jar
#WORKDIR app
#ADD ./build/libs/sps-api-1.0.jar /app/
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/sps-api-1.0.jar"]
ENTRYPOINT ["java", "-jar", "/sps_api.jar"]