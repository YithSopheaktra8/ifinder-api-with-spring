package co.istad.ifinder.features.ai.geminiresponse;


import java.util.List;

public class Content {
    private List<Part> parts;
    private String role;

    // Getters and setters
    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

