FROM amazoncorretto:11-alpine

ENV SERVER_SERVLET_CONTEXT_PATH=/ecsdemo

ARG JAR_FILE=target/my-greeting-web-1.0.0.jar
ADD ${JAR_FILE} my-greeting-web.jar
ENTRYPOINT ["java", "-jar", "/my-greeting-web.jar"]