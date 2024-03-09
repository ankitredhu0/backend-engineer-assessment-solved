FROM openjdk:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app-0.0.1-SNAPSHOT.jar"]