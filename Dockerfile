FROM openjdk:11-jre-slim-buster
WORKDIR /app
COPY . /app
RUN ./gradlew build
CMD ["java", "-jar", "app/build/libs/c2-search-netlas-1.0-SNAPSHOT.jar"]
