package com.ovrckd.llm.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ovrckd.discord.listener.dto.DiscordMessageDto;
import com.ovrckd.discord.sender.dto.DiscordMessagingOrchestratorDto;
import com.ovrckd.llm.engine.GeminiRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class GeminiRequestFactory {

    private final ObjectMapper objectMapper;

    @Value("${bot.name}")
    private String botName;

    @SneakyThrows
    public GeminiRequest createDecisionRequest(List<DiscordMessageDto> recentMessageHistory) {
        var promptTemplate = new PromptTemplate(new ClassPathResource("prompts/analysis/response-analysis-user-prompt.st"));
        promptTemplate.add("botName", botName);
        promptTemplate.add("mostRecentMessage", objectMapper.writeValueAsString(recentMessageHistory.getLast()));
        promptTemplate.add("recentMessageHistory", objectMapper.writeValueAsString(recentMessageHistory));
        var prompt = promptTemplate.render();

        var systemPrompt = new PromptTemplate(new ClassPathResource("prompts/analysis/response-analysis-system-prompt.st")).render();

        return GeminiRequest.builder()
                .systemPrompt(systemPrompt)
                .userPrompt(prompt)
                .temperature(0.0)
                .build();
    }

    @SneakyThrows
    public GeminiRequest createMessageGenerateRequest(String reasonToSendMessage, List<DiscordMessageDto> recentMessageHistory) {
        var userPromptTemplate = new PromptTemplate(new ClassPathResource("prompts/generate/message-generate-user-prompt.st"));
        userPromptTemplate.add("reasonToSendMessage", reasonToSendMessage);
        userPromptTemplate.add("recentMessageHistory", objectMapper.writeValueAsString(recentMessageHistory));
        userPromptTemplate.add("botName", botName);
        var userPrompt = userPromptTemplate.render();

        var systemPrompt = new PromptTemplate(new ClassPathResource("prompts/generate/message-generate-system-prompt.st")).render();

        return GeminiRequest.builder()
                .systemPrompt(systemPrompt)
                .userPrompt(userPrompt)
                .temperature(1.0)
                .build();
    }
}
