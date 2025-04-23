package com.ovrckd.llm.engine;

import com.ovrckd.llm.model.ModelFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ovrckd.utils.StringUtils.unwrapJSONFromWindowMarkdown;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final ModelFactory modelFactory;

    public GeminiResponse process(GeminiRequest request) {
        var chatModel = modelFactory.create(request);

        var promptMessages = new ArrayList<Message>();
        if (!isBlank(request.systemPrompt())) {
            promptMessages.add(new SystemMessage(request.systemPrompt()));
        }
        promptMessages.add(new UserMessage(request.userPrompt()));

        var response = chatModel.call(new Prompt(promptMessages));
        return GeminiResponse.builder()
                .response(unwrapJSONFromWindowMarkdown(response.getResult().getOutput().getText()))
                .inputTokens(response.getMetadata().getUsage().getPromptTokens())
                .outputTokens(response.getMetadata().getUsage().getCompletionTokens())
                .build();
    }
}
