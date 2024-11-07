FROM openjdk:11
EXPOSE 8089
WORKDIR /app
ADD target/kaddem-1.jar  kaddem-1.jar
ENTRYPOINT ["java", "-jar", "kaddem-1.jar"]