FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY build/libs/genai-rest-api-jira-integration-for-testcase-creation-0.0.1-SNAPSHOT.jar genairestapijiraintegrationfortestcasecreation.jar
EXPOSE 3000
ENTRYPOINT exec java $JAVA_OPTS -jar genairestapijiraintegrationfortestcasecreation.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar genairestapijiraintegrationfortestcasecreation.jar
