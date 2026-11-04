# Etapa 1: Compilar la aplicacion
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

# Etapa 2: Ejecutar la aplicacion
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/tienda-celulares-1.0.0.war app.war

EXPOSE 10000

ENV PORT=10000

ENTRYPOINT ["sh", "-c", "exec java -Dserver.port=${PORT:-10000} -jar /app/app.war"]
