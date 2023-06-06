FROM amazoncorretto:17.0.7-alpine

ARG VERSION

COPY build/libs/bank-management-$VERSION.jar /usr/app/bank-management-$VERSION.jar

WORKDIR /usr/app

RUN sh -c 'touch bank-management-$VERSION.jar'

# Specify the command to run your application
CMD ["java", "-jar", "bank-management-$VERSION.jar"]