package com.ovrckd.discord.sender.dto;

import lombok.Builder;

@Builder
public record DiscordMessageSendRequest(String channelId, String message) {}
