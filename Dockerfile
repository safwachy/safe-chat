FROM openjdk:11-jdk-slim

EXPOSE 8080

RUN mkdir /app

ARG JAR_FILE=build/libs/safechat-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /app/safe-chat-api.jar

ENTRYPOINT ["java","-jar","/app/safe-chat-api.jar"]