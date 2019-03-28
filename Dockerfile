FROM openjdk:8-jre-alpine

ENV SERVER_SERVLET_CONTEXT_PATH=/ecsdemo

ARG JAR_FILE=target/my-greeting-web-0.1.0.jar
ADD ${JAR_FILE} my-greeting-web.jar
ENTRYPOINT ["java", "-jar", "/my-greeting-web.jar"]