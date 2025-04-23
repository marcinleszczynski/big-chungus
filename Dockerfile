FROM openjdk:23
ADD build/libs/discord-bot-1.0-SNAPSHOT-all.jar docker-ovrckd.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "docker-ovrckd.jar"]