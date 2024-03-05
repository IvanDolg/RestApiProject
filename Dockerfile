FROM eclipse-temurin:17.0.4_8-jre
COPY /target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]