FROM adoptopenjdk/openjdk16:alpine-jre
WORKDIR /app
COPY ./app/build/libs/app-all.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
