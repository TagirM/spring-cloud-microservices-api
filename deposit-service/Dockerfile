FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 9999
COPY build/libs/deposit-service-1.0-SNAPSHOT.jar .
RUN apk add --no-cache bash
COPY wait-for.sh /wait-for.sh
RUN chmod +x /wait-for.sh
ENTRYPOINT ["/wait-for.sh", "bill-service:8082", "--timeout=210", "--", "java", "-jar", "deposit-service-1.0-SNAPSHOT.jar"]