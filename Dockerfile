FROM openjdk:11
MAINTAINER nemanja
EXPOSE 3003
ARG JAR_FILE=build/libs/account-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "-Xms512M","-Xmx512M", "api.jar"]
