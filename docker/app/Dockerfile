FROM eclipse-temurin:25-jre

COPY docker/app/opentelemetry-javaagent.jar /opt/opentelemetry-javaagent.jar

COPY target/*.jar /app.jar

ENTRYPOINT ["java", "-javaagent:/opt/opentelemetry-javaagent.jar", "-jar", "/app.jar"]