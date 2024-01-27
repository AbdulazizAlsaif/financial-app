FROM maven:3-eclipse-temurin-21-alpine AS build
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN mvn -DskipTests=true clean package


FROM amazoncorretto:17.0.7-alpine
ARG JAR_FILE=/app/target/*.jar
COPY --from=build $JAR_FILE /app/app.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/app.jar
