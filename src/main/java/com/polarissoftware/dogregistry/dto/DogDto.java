package com.polarissoftware.dogregistry.dto;

import com.polarissoftware.dogregistry.model.DogStatus;
import com.polarissoftware.dogregistry.model.Gender;
import com.polarissoftware.dogregistry.model.LeavingReason;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Instant;
import java.time.LocalDate;

@Serdeable
public record DogDto(
        Long id,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Breed is required")
        String breed,

        @NotBlank(message = "Breed is required")
        String supplier,

        @NotBlank(message = "Badge Id is required")
        String badgeId,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotNull(message = "Birth date is required")
        @PastOrPresent(message = "Birth date cannot be in the future")
        LocalDate birthDate,

        @NotNull(message = "Date acquired is required")
        @PastOrPresent(message = "Date acquired cannot be in the future")
        LocalDate dateAcquired,

        @NotNull(message = "Current status is required")
        DogStatus currentStatus,

        @PastOrPresent(message = "Leaving date cannot be in the future")
        LocalDate leavingDate,
        LeavingReason leavingReason,
        String kennellingCharacteristic,

        Instant dateCreated,
        Instant dateModified
) {}
