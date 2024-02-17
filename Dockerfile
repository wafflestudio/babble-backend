FROM openjdk:11-jre-slim-buster

RUN mkdir /babble-server
COPY ./build/libs/babble-0.0.1.jar /babble-server/

WORKDIR /babble-server

ARG PROFILE
ARG JWT_SECRET_KEY
ARG JWT_VALIDITY
ARG MYSQL_URL
ARG MYSQL_DB
ARG MYSQL_USERNAME
ARG MYSQL_PASSWORD
ARG TOO_CLOSE_LIMIT

ENV PROFILE=$PROFILE \
    JWT-SECRET-KEY=$JWT_SECRET_KEY \
    JWT-VALIDITY=$JWT_VALIDITY \
    MYSQL-URL=$MYSQL_URL \
    MYSQL-DB=$MYSQL_DB \
    MYSQL-USERNAME=$MYSQL_USERNAME \
    MYSQL-PASSWORD=$MYSQL_PASSWORD \
    TOO-CLOSE-LIMIT=$TOO_CLOSE_LIMIT

CMD ["nohup", "java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-DJWT_SECRET_KEY=${JWT-SECRET-KEY}", "-DJWT_VALIDITY=${JWT-VALIDITY}", "-DMYSQL_URL=${MYSQL-URL}", "-DMYSQL_DB=${MYSQL-DB}", "-DMYSQL_USERNAME=${MYSQL-USERNAME}", "-DMYSQL_PASSWORD=${MYSQL-PASSWORD}", "-DLOCATION_VALIDATION_LIMIT=${TOO-CLOSE-LIMIT}", "/babble-server/babble-0.0.1.jar", "&"]
