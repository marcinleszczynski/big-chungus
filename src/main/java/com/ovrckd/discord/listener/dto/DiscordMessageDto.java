package com.ovrckd.discord.listener.dto;

import lombok.Builder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Builder
public record DiscordMessageDto(String author, String text, String createdAt) {
    public static List<DiscordMessageDto> fromHistory(MessageHistory messageHistory) {
        return messageHistory.getRetrievedHistory()
                .stream()
                .sorted(Comparator.comparing(Message::getTimeCreated))
                .map(DiscordMessageDto::fromMessage)
                .filter(message -> !isBlank(message.text()))
                .toList();
    }

    public static DiscordMessageDto fromMessage(Message message) {
        return DiscordMessageDto.builder()
                .author(message.getAuthor().getEffectiveName())
                .text(message.getContentDisplay())
                .createdAt(message.getTimeCreated().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .build();
    }
}
