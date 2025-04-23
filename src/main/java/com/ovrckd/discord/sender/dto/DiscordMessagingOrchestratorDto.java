package com.ovrckd.discord.sender.dto;

import com.ovrckd.discord.listener.dto.DiscordMessageDto;
import lombok.Builder;

import java.util.List;

@Builder
public record DiscordMessagingOrchestratorDto(String channelId, String reasonToSendMessage, List<DiscordMessageDto> history) {}
