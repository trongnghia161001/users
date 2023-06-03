FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} usersservice.jar

ENTRYPOINT ["java", "-jar", "/usersservice.jar"]

EXPOSE 9036