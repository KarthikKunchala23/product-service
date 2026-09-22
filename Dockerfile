FROM eclipse-temurin:21-jre

RUN groupadd --system spring && \
    useradd --system --gid spring spring

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]