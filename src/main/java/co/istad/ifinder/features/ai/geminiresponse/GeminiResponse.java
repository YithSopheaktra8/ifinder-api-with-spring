package co.istad.ifinder.features.ai.geminiresponse;

import java.util.List;

public class GeminiResponse {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;

    // Getters and setters
    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public UsageMetadata getUsageMetadata() {
        return usageMetadata;
    }

    public void setUsageMetadata(UsageMetadata usageMetadata) {
        this.usageMetadata = usageMetadata;
    }
}

