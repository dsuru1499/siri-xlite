FROM openjdk
COPY target/siri-xlite-1.0-SNAPSHOT.jar siri-xlite-1.0-SNAPSHOT.jar
EXPOSE 8443
ENTRYPOINT ["java","-jar","siri-xlite-1.0-SNAPSHOT.jar","--spring.data.mongodb.host=mongodb"]

