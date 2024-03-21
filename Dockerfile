FROM eclipse-temurin:17-alpine

VOLUME /tmp

EXPOSE 8080

WORKDIR /desafiosicred

COPY build/libs/*.jar desafiosicred.jar

ENTRYPOINT ["java","-jar","gj-be.jar"]