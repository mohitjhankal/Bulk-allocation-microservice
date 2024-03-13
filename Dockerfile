FROM openjdk:17-alpine
EXPOSE 8080
ARG JAR_FILE=target/nb-bulk-allocation-1.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]