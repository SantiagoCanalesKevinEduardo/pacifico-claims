# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar el pom.xml
COPY pom.xml .

# Descargar las dependencias usando 'dependency:resolve' para aprovechar el cache.
RUN --mount=type=cache,target=/root/.m2 mvn dependency:resolve -B

# Copiar el código fuente
COPY src ./src

# Compilar la aplicación reutilizando el caché de dependencias de Maven
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests -B

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el jar generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto por defecto de la aplicación
EXPOSE 8080

# Comando de inicio directo
ENTRYPOINT ["java", "-jar", "app.jar"]
