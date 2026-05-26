FROM gradle:8.13-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY scheduler/build.gradle /app/scheduler/

WORKDIR /app/scheduler
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

WORKDIR /app
COPY scheduler/src /app/scheduler/src
WORKDIR /app/scheduler
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/scheduler/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
