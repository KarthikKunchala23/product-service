FROM eclipse-temurin:21-jre-alpine

RUN apk upgrade --no-cache

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]