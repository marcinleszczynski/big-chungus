package com.ovrckd.llm.engine.response;

public record AnalysisResponse(Boolean decision, String explanation) {
    public Boolean shouldSendMessage() {
        return decision();
    }
}
