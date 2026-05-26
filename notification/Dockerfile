FROM gradle:8.13-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY notification/build.gradle /app/notification/

WORKDIR /app/notification
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

WORKDIR /app
COPY notification/src /app/notification/src
WORKDIR /app/notification
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/notification/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
