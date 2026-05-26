FROM gradle:8.13-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY task/build.gradle /app/task/

WORKDIR /app/task
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

WORKDIR /app
COPY task/src /app/task/src
WORKDIR /app/task
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/task/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
