package com.polarissoftware.dogregistry.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum LeavingReason {
    TRANSFERRED("Transferred"),
    RETIRED_PUT_DOWN("Retired (Put Down)"),
    KIA("KIA"),
    REJECTED("Rejected"),
    RETIRED_RE_HOUSED("Retired (Re-housed)"),
    DIED("Died");

    private final String value;

    LeavingReason(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LeavingReason fromString(String text) {
        if (text == null) return null;

        return Arrays.stream(LeavingReason.values())
                .filter(reason -> reason.value.equalsIgnoreCase(text.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown leaving reason: " + text));
    }
}
