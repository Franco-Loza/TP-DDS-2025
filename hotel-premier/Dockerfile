# Imagen simple de Java para ejecutar el JAR
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR compilado (debe existir en target/)
COPY target/hotel-premier-0.0.1-SNAPSHOT.jar app.jar

# Crear directorio de logs
RUN mkdir -p /app/logs

EXPOSE 8080

# Ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
