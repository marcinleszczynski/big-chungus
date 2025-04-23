package com.ovrckd.llm.engine;

import lombok.Builder;

@Builder
public record GeminiRequest(Double temperature, String systemPrompt, String userPrompt) {}
