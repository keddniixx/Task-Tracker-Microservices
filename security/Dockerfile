FROM gradle:8.13-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY security/build.gradle /app/security/

WORKDIR /app/security
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

WORKDIR /app
COPY security/src /app/security/src
WORKDIR /app/security
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/security/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
