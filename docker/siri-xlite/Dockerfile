FROM openjdk
WORKDIR /tmp
ADD target/siri-xlite-1.0-SNAPSHOT.jar .
ADD docker/siri-xlite/data.zip .
EXPOSE 8443
ENTRYPOINT ["java","-Duser.timezone=Europe/Paris", "-jar","siri-xlite-1.0-SNAPSHOT.jar","--spring.data.mongodb.host=mongodb"]

