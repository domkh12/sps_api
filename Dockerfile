   # Stage 1: Build
FROM ghcr.io/graalvm/jdk-community:21 AS build
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew
RUN ./gradlew clean build

   # Stage 2: Run
FROM ghcr.io/graalvm/jdk-community:21
WORKDIR /app
COPY --from=build /app/build/libs/sps-api-1.0.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/sps-api-1.0.jar"]