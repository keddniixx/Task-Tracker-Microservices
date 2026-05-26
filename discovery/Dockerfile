FROM gradle:8.13-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY discovery/build.gradle /app/discovery/

WORKDIR /app/discovery
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

WORKDIR /app
COPY discovery/src /app/discovery/src
WORKDIR /app/discovery
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/discovery/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
