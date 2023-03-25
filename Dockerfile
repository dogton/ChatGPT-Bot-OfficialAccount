FROM maven:3.6-jdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline
RUN mvn package -DskipTests

EXPOSE 80
ENTRYPOINT ["java", "-jar", "/app/target/ChatGPT-Bot-0.0.1-SNAPSHOT.jar"]