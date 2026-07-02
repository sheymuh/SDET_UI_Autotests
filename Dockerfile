FROM maven:3.9.6-eclipse-temurin-21-alpine

RUN apk add --no-cache \
    bash \
    curl \
    fontconfig \
    ttf-dejavu

WORKDIR /ui-autotests

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mkdir -p /ui-autotests/target/allure-results /ui-autotests/target/surefire-reports

ARG suiteFile=testng-cross-browser-selenoid.xml

ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2/repository"

RUN mvn clean compile

CMD mvn test -Dsurefire.suiteXmlFiles=src/test/resources/configurations/${suiteFile} -e