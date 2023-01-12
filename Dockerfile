FROM amazoncorretto:11-alpine-jdk
ADD /build/libs/car-pooling-1.0-SNAPSHOT.jar car-pooling-1.0-SNAPSHOT.jar
EXPOSE 9091
ENTRYPOINT ["java","-jar","/car-pooling-1.0-SNAPSHOT.jar","--server.port=9091"]