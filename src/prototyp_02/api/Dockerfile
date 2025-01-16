FROM gradle:jdk17-alpine AS build
WORKDIR /home/gradle/app
COPY . /home/gradle/app
ENV GRADLE_USER_HOME=/home/gradle/.gradle
RUN --mount=type=cache,target=${GRADLE_USER_HOME}\
    gradle bootJar --no-daemon


FROM openjdk:17-jdk-alpine AS prod
WORKDIR /home/gradle/app
COPY --from=build /home/gradle/app/build/libs /home/gradle/app
CMD ["java", "-jar","api-0.0.1-SNAPSHOT.jar"]
