FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} mg-football-be.jar
ENTRYPOINT ["java","-jar","/mg-football-be.jar"]
