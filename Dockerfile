FROM amazoncorretto:17.0.7-alpine

ARG VERSION

COPY build/libs/bank-management-${VERSION}.jar /usr/app/bank-management.jar

WORKDIR /usr/app

CMD ["java", "-jar", "bank-management.jar"]
