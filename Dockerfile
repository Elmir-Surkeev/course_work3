FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/db/changelog /db/changelog
ENTRYPOINT ["java", "-jar", "/app.jar"]
