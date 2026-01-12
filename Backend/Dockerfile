# Étape 1 : builder le projet
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : image finale
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
ENV PORT=8080
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","/app/app.jar"]
