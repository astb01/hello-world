FROM openjdk:11-jdk-slim
LABEL maintainer="Assad Ahmad"
LABEL service="hello-world"
VOLUME /tmp
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]