FROM alpine/java:21-jdk AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN apk add --no-cache bash
RUN dos2unix gradlew
# RUN chmod +x gradlew

COPY src src

RUN bash ./gradlew shadowJar --no-daemon

FROM alpine/java:21-jre
WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]