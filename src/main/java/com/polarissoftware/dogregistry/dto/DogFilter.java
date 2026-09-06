package com.polarissoftware.dogregistry.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record DogFilter(
        String name,
        String breed,
        String supplier
) {}
