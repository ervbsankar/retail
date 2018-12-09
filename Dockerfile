# load the jenkins from latest
FROM openjdk:8-jdk-alpine

RUN echo 'open jdk is built'

# Maintainer Info
MAINTAINER Sankar bhimeswara.edala@gmail.com

ARG JAR_FILE=target/retail-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} retail.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/retail.jar"]
