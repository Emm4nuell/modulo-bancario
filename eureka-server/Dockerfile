FROM openjdk:17
WORKDIR app/
COPY ./target/eureka-server-0.0.1-SNAPSHOT.jar eureka.jar
EXPOSE 8761
CMD ["java", "-jar", "eureka.jar"]