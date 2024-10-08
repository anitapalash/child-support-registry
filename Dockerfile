#Build
FROM maven:3.8.5-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#Package
FROM openjdk:17-oracle
COPY --from=build /home/app/target/child-support-registry-0.0.1-SNAPSHOT.jar /usr/local/lib/child-support-registry-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/child-support-registry-0.0.1-SNAPSHOT.jar"]