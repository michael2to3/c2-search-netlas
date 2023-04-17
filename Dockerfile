FROM adoptopenjdk/openjdk16:x86_64-alpine-jre-16.0.1_9
WORKDIR /app
COPY . /app
RUN ./gradlew build
CMD ["java", "-jar", "app/build/libs/c2-search-netlas-1.0-SNAPSHOT.jar"]
