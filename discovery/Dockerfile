FROM gradle:8.10-jdk17 AS builder
WORKDIR /app

RUN echo "pluginManagement { repositories { maven { url 'https://maven-central.storage-download.googleapis.com/maven2/' }; mavenCentral(); gradlePluginPortal() } }" > settings.gradle

COPY discovery/build.gradle ./build.gradle
COPY discovery/src ./src

RUN gradle bootJar --no-daemon -x test \
    --max-workers=2 \
    -Dorg.gradle.internal.http.socketTimeout=120000 \
    -Dorg.gradle.internal.http.connectionTimeout=120000

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]