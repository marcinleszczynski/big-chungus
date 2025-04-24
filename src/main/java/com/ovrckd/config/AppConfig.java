package com.ovrckd.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Slf4j
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("com.ovrckd")
public class AppConfig {

    @Value("${gemini.token}")
    private String geminiToken;

    @Value("${gemini.base-url}")
    private String baseUrl;

    @Value("${gemini.completions-path}")
    private String completionsPath;

    @Value("${bot.token}")
    private String botToken;

    @Bean
    public OpenAiApi openAiApi() {
        return OpenAiApi.builder()
                .apiKey(geminiToken)
                .baseUrl(baseUrl)
                .completionsPath(completionsPath)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JDA jda() {
        return JDABuilder.createDefault(botToken)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
    }
}
