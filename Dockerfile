FROM maven:3.6-jdk-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=build /app/target/ChatGPT-Bot-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/app.jar"]