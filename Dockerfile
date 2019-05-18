FROM openjdk:11
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://mongo-container:27017/", "-Dspring.profiles.active=prod","-jar","/app.jar"]