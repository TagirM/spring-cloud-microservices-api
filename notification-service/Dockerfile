FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/notification-service-1.0-SNAPSHOT.jar .
RUN apk add --no-cache bash
COPY wait-for.sh /wait-for.sh
RUN chmod +x /wait-for.sh
ENTRYPOINT ["/wait-for.sh", "bill-service:8082", "--timeout=250", "--", "java", "-jar", "notification-service-1.0-SNAPSHOT.jar"]