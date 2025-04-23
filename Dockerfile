FROM openjdk:23
ADD build/libs/discord-bot-1.0-SNAPSHOT-all.jar ovrckd-bot.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "ovrckd-bot.jar"]