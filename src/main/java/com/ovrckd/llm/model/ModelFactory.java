package com.ovrckd.llm.model;

import com.ovrckd.llm.engine.GeminiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class ModelFactory {

    @Value("${gemini.model}")
    private String model;

    private final OpenAiApi openAiApi;

    public ChatModel create(GeminiRequest request) {
        var options = OpenAiChatOptions.builder()
                .model(model)
                .temperature(request.temperature())
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();
    }
}
