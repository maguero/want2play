FROM maven:3.6.2-jdk-13 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /build/src/
RUN mvn package

FROM openjdk:13-jdk-alpine as Target
COPY --from=builder /build/target/want2play-0.0.1-SNAPSHOT.jar want2play.jar
ENTRYPOINT ["java","-jar","want2play.jar"]
EXPOSE 9001