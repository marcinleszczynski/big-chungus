package com.ovrckd.discord.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ovrckd.discord.listener.dto.DiscordMessageDto;
import com.ovrckd.discord.sender.DiscordMessagingOrchestrator;
import com.ovrckd.discord.sender.dto.DiscordMessagingOrchestratorDto;
import com.ovrckd.llm.engine.GeminiService;
import com.ovrckd.llm.engine.response.AnalysisResponse;
import com.ovrckd.llm.factory.GeminiRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ovrckd.utils.SpringUtils.isDevelopment;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class BigChungusMessageListener extends ListenerAdapter {

    private final GeminiRequestFactory geminiRequestFactory;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;
    private final DiscordMessagingOrchestrator messagingOrchestrator;

    @Value("bot.name")
    private String botName;

    @Override
    @SneakyThrows
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (verifyIfShouldNotReply(event)) {
            return;
        }

        var discordHistory = event.getChannel().getHistoryBefore(event.getMessageId(), 10).complete();
        var recentMessages = new ArrayList<>(DiscordMessageDto.fromHistory(discordHistory));
        recentMessages.add(DiscordMessageDto.fromMessage(event.getMessage()));

        var analysisRequest = geminiRequestFactory.createDecisionRequest(recentMessages);
        var geminiResponse = geminiService.process(analysisRequest);
        var analysisResponse = objectMapper.readValue(geminiResponse.response(), AnalysisResponse.class);

        log.info("\n\nresponse: \n{}\n{}\n{}\n{}", analysisResponse.decision(), analysisResponse.explanation(), geminiResponse.inputTokens(), geminiResponse.outputTokens());

        if (analysisResponse.shouldSendMessage()) {
            var messagingOrchestratorRequest = DiscordMessagingOrchestratorDto.builder()
                    .reasonToSendMessage(analysisResponse.explanation())
                    .history(recentMessages)
                    .channelId(event.getChannel().getId())
                    .build();
            messagingOrchestrator.orchestrate(messagingOrchestratorRequest);
        }
    }

    private boolean verifyIfShouldNotReply(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return true;
        }

        if (isBlank(event.getMessage().getContentDisplay())) {
            return true;
        }

        if (isDevelopment() && event.getChannel().getId().equals("1363445136213938190")) {
            return true;
        }

        return event.getAuthor().getEffectiveName().equals(botName);
    }
}