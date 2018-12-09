# MyRetail

This project was generated with Spring boot 2.1.1

## Development server

Import into Intellij Idea or Eclipse and run the RetailApplication.class as Java application or Spring boot appliation.

## Build

Run `mvn clean package -Dmaven.test.skip=true` to build the project. The build jar artifact will be stored in the `target/` directory. 

## Running unit tests

Execute the unit tests available in src/main/test.

## Deployment
 1. Run with embedded tomcat then simple run the jar with external application.yml file
  java -jar target/retail-0.0.1-SNAPSHOT.jar --Dspring.config.location=
 2. Run in docker container
  docker-compose up --build
  docker ps -a (to identify docker container)
  