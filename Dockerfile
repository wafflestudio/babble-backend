FROM openjdk:11-jdk-slim-sid

RUN mkdir /babble-server
ADD . /babble-server
WORKDIR /babble-server

RUN	./gradlew build

CMD ["nohup", "java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-DKAKAO_CLIENT_ID=${CLIENT_ID}", "/babble-server/build/libs/babble-0.0.1.jar", "&"]
