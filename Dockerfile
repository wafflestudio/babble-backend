FROM openjdk:11-jre-slim-buster

RUN mkdir /babble-server
COPY ./build/libs/babble-0.0.1.jar /babble-server/

WORKDIR /babble-server

CMD ["nohup", "java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-DJWT_SECRET_KEY=${JWT-SECRET-KEY}", "-DJWT_VALIDITY=${JWT-VALIDITY}", "-DMYSQL_URL=${MYSQL-URL}", "-DMYSQL_DB=${MYSQL-DB}", "-DMYSQL_USERNAME=${MYSQL-USERNAME}", "-DMYSQL_PASSWORD=${MYSQL-PASSWORD}", "-DLOCATION_VALIDATION_LIMIT=${TOO-CLOSE-LIMIT}", "/babble-server/babble-0.0.1.jar", "&"]
