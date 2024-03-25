FROM openjdk:17-alpine

ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

WORKDIR application

COPY ../../../target/RestMvcProject-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-jar", "RestMvcProject-0.0.1-SNAPSHOT.jar"]