FROM openjdk:17.0.1-jdk-slim

RUN apt-get update && apt-get install -y dos2unix

COPY . /opt/app

WORKDIR /opt/app

RUN dos2unix gradlew

RUN bash gradlew build -x test

RUN cp build/libs/delivery-service.jar ./delivery-service.jar

ENTRYPOINT ["java","-jar","delivery-service.jar"]