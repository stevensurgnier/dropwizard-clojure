package com.example.helloworld.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class Todo {
    @NotEmpty
    private boolean complete;
    @NotEmpty
    private String description;

    public Todo() {
        // Jackson deserialization
    }

    public Todo(boolean complete, String description) {
        this.complete = complete;
        this.description = description;
    }

    @JsonProperty
    public boolean getComplete() {
        return complete;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
}
