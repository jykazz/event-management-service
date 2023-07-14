FROM amazoncorretto:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir /resources
#COPY ./src/main/resources/application.properties /app/config/application.properties
ENTRYPOINT ["java","-Dspring.config.location=/resources/", "-jar","/app.jar"]
#ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=file:/app/config/application.properties"]