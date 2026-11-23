# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Leverage Docker layer caching for dependencies
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# Create non-root user
RUN useradd -ms /bin/bash appuser

COPY --from=build /build/target/akash-priya-wedding.jar app.jar

# Directories for persistent data (mount a volume here in production)
RUN mkdir -p /app/data /app/uploads && chown -R appuser:appuser /app

USER appuser

ENV SERVER_PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

