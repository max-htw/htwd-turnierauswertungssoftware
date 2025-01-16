FROM gradle:jdk17-alpine AS build
WORKDIR /gradle/src/api
COPY . /gradle/src/api/
RUN ["rm", "-rf", "build/"]
RUN ["gradle", "bootJar"]

FROM openjdk:17-jdk-alpine AS prod
WORKDIR /gradle
COPY --from=build /gradle/src/api/build/libs /gradle
CMD ["java", "-jar","api-0.0.1-SNAPSHOT.jar"]