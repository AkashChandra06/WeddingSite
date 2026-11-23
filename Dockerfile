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

COPY --from=build /build/target/akash-priya-wedding.jar app.jar
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

# NOTE: Running as root here on purpose. Railway/Render mount persistent
# volumes at /app/data at container start, which resets ownership to root
# and would break a non-root user's write access. The entrypoint script
# creates the required directories at runtime (not build time), since a
# freshly mounted empty volume would otherwise hide anything baked in here.

# Railway/Render inject PORT at runtime; app listens on it via server.port=${PORT:8080}
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["/app/docker-entrypoint.sh"]

