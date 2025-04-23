package com.ovrckd.discord.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ovrckd.discord.sender.dto.DiscordMessageSendRequest;
import com.ovrckd.discord.sender.dto.DiscordMessagingOrchestratorDto;
import com.ovrckd.llm.engine.GeminiService;
import com.ovrckd.llm.engine.response.MessageGenerateResponse;
import com.ovrckd.llm.factory.GeminiRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordMessagingOrchestrator {

    private final DiscordMessageSendService discordMessageSendService;
    private final GeminiRequestFactory geminiRequestFactory;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public void orchestrate(DiscordMessagingOrchestratorDto dto) {
        var message = generateMessage(dto);
        sendDiscordMessage(dto, message);
    }

    @SneakyThrows
    private String generateMessage(DiscordMessagingOrchestratorDto dto) {
        var request = geminiRequestFactory.createMessageGenerateRequest(dto.reasonToSendMessage(), dto.history());
        var geminiResponse = geminiService.process(request);
        var generatedMessage = objectMapper.readValue(geminiResponse.response(), MessageGenerateResponse.class);
        return generatedMessage.message();
    }

    private void sendDiscordMessage(DiscordMessagingOrchestratorDto dto, String message) {
        var request = DiscordMessageSendRequest.builder()
                .message(message)
                .channelId(dto.channelId())
                .build();
        discordMessageSendService.process(request);
    }
}
