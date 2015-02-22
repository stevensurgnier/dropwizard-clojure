package com.example.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.constraints.NotNull;

public class TodoConfiguration extends Configuration {
    @NotNull
    private int maxSize;

    @JsonProperty
    public int getMaxSize() {
        return maxSize;
    }

    @JsonProperty
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
