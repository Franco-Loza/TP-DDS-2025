# Flujo de trabajo con Docker

## Primera vez
cd hotel-premier
./mvnw clean package -DskipTests
cd ..
docker-compose build
docker-compose up -d

## Cuando cambiemos algo del back de Spring
cd hotel-premier
./mvnw clean package -DskipTests
cd ..
docker-compose build hotel-premier
docker-compose up -d --force-recreate --no-deps hotel-premier

## Para bajar los contenedores porque terminamos de trabajar
docker-compose down

## Cuando ocurre un error y nada funciona (elimina todos los volumenes)
docker-compose down -v
// Empieza desde el primer paso (Primera vez)