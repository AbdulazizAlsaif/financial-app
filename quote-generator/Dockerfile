FROM maven:3-eclipse-temurin-11 AS build
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN mvn -DskipTests=true clean package


FROM amazoncorretto:11.0.22-alpine3.19
ARG JAR_FILE=/app/target/*.jar
ARG LIB_LOCATION=/app/target/lib
COPY --from=build $JAR_FILE /app/app.jar
COPY --from=build $LIB_LOCATION /app/lib
ENTRYPOINT java -jar /app/app.jar
