package com.polarissoftware.dogregistry.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Arrays;

@Serdeable
public enum DogStatus {
    IN_TRAINING("In Training"),
    IN_SERVICE("In Service"),
    RETIRED("Retired"),
    LEFT("Left");

    private final String value;

    DogStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static DogStatus fromString(String text) {
        if (text == null) return null;

        return Arrays.stream(DogStatus.values())
                .filter(status -> status.value.equalsIgnoreCase(text.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + text));
    }
}
