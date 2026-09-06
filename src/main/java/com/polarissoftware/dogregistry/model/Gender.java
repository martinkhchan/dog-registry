package com.polarissoftware.dogregistry.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Arrays;

@Serdeable
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Gender fromString(String text) {
        if (text == null) return null;

        return Arrays.stream(Gender.values())
                .filter(gender -> gender.value.equalsIgnoreCase(text.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown gender: " + text));
    }
}
