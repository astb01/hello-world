FROM openjdk:11-jdk-slim
LABEL maintainer="Assad Ahmad"
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]