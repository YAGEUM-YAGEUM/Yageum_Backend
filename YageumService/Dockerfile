FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Djasypt.encryptor.password=${JASYPT_PASSWORD}","/app.jar"]