package com.example.todo.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Todo {
    @NotNull
    private Boolean complete;
    @NotEmpty
    private String description;

    public Todo() {
        // Jackson deserialization
    }

    public Todo(Boolean complete, String description) {
        this.complete = complete;
        this.description = description;
    }

    @JsonProperty
    public Boolean getComplete() {
        return complete;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
}
