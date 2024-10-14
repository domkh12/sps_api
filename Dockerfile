# PHASE 1 - Download & Install JDK

FROM ghcr.io/graalvm/jdk-community:21 AS build
COPY . .
RUN gradle build

FROM openjdk:21-jdk-slim
COPY --from=build /target/sps_api-1.0.jar sps_api.jar
#WORKDIR app
#ADD ./build/libs/sps-api-1.0.jar /app/
EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/sps-api-1.0.jar"]
ENTRYPOINT ["java", "-jar", "sps_api.jar"]