FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8082
COPY build/libs/bill-service-1.0-SNAPSHOT.jar .
RUN apk add --no-cache bash
COPY wait-for.sh /wait-for.sh
RUN chmod +x /wait-for.sh
ENTRYPOINT ["/wait-for.sh", "account-service:8081", "--timeout=190", "--", "java", "-jar", "bill-service-1.0-SNAPSHOT.jar"]