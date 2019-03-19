FROM tomcat:9.0.1-alpine

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY target/my-greeting-web-0.1.0.war /usr/local/tomcat/webapps/ecsdemo.war