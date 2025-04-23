package com.ovrckd.llm.engine;

import lombok.Builder;

@Builder
public record GeminiResponse(String response, Integer inputTokens, Integer outputTokens) {}
