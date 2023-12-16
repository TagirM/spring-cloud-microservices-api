FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8001
COPY build/libs/config-service-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "config-service-1.0-SNAPSHOT.jar"]