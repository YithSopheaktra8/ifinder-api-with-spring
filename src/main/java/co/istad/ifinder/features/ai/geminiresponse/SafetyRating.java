package co.istad.ifinder.features.ai.geminiresponse;

public class SafetyRating {
    private String category;
    private String probability;

    // Getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }
}

