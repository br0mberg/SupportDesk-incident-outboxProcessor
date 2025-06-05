FROM maven:3.9.9-eclipse-temurin-24 AS builder
WORKDIR /SupportDesk-incident-outboxProcessor
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean install -e


FROM openjdk:24-jdk-slim
WORKDIR /Incident-outboxProcessor
COPY --from=builder /Incident-outboxProcessor/target/*.jar /Incident-outboxProcessor/Incident-outboxProcessor-app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/SupportDesk-incident-outboxProcessor/SupportDesk-incident-outboxProcessor-app.jar"]