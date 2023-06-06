FROM amazoncorretto:17.0.7-alpine

COPY build/libs/bank-management-1.0.1.jar usr/app/bank-management-1.0.1.jar

WORKDIR /usr/app

RUN sh -c 'touch bank-management-1.0.1.jar'

# Specify the command to run your application
CMD ["java", "-jar", "bank-management-1.0.1.jar"]