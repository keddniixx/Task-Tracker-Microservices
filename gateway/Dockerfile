FROM gradle:8.13-jdk17-alpine AS builder
WORKDIR /app

# Step 1: Copy only build configuration files
COPY build.gradle settings.gradle /app/
COPY gateway/build.gradle /app/gateway/

# Step 2: Download dependencies to cache them
WORKDIR /app/gateway
RUN gradle dependencies --no-daemon

# Step 3: Copy source code and build
WORKDIR /app
COPY gateway/src /app/gateway/src
WORKDIR /app/gateway
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/gateway/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]