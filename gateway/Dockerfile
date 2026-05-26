FROM gradle:8.13-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY gateway/build.gradle /app/gateway/

WORKDIR /app/gateway
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

WORKDIR /app
COPY gateway/src /app/gateway/src
WORKDIR /app/gateway
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/gateway/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
