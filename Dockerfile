FROM openjdk:latest
ADD target/rancard-assessment.jar rancard-assessment.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","rancard-assessment.jar"]