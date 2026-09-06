package com.polarissoftware.dogregistry.model;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;

import java.time.Instant;
import java.time.LocalDate;

@MappedEntity("DOGS")
public class DogEntity {

    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long id;

    private String name;
    private String breed;
    private String supplier;
    private String badgeId;
    private Gender gender;
    private LocalDate birthDate;
    private LocalDate dateAcquired;
    private DogStatus currentStatus;
    @Nullable
    private LocalDate leavingDate;
    @Nullable
    private LeavingReason leavingReason;
    @Nullable
    private String kennellingCharacteristic;

    // --- Soft delete flag ---
    private boolean deleted = false;

    // --- Audit timestamps ---
    @DateCreated
    private Instant dateCreated;
    @DateUpdated
    private Instant dateModified;

    public DogEntity() {
    }

    public DogEntity(Long id, String name, String breed, String supplier, String badgeId, Gender gender,
                     LocalDate birthDate, LocalDate dateAcquired, DogStatus currentStatus, LocalDate leavingDate,
                     LeavingReason leavingReason, String kennellingCharacteristic, boolean deleted,
                     Instant dateCreated, Instant dateModified) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.supplier = supplier;
        this.badgeId = badgeId;
        this.gender = gender;
        this.birthDate = birthDate;
        this.dateAcquired = dateAcquired;
        this.currentStatus = currentStatus;
        this.leavingDate = leavingDate;
        this.leavingReason = leavingReason;
        this.kennellingCharacteristic = kennellingCharacteristic;
        this.deleted = deleted;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(LocalDate dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public DogStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(DogStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public LocalDate getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(LocalDate leavingDate) {
        this.leavingDate = leavingDate;
    }

    public LeavingReason getLeavingReason() {
        return leavingReason;
    }

    public void setLeavingReason(LeavingReason leavingReason) {
        this.leavingReason = leavingReason;
    }

    public String getKennellingCharacteristic() {
        return kennellingCharacteristic;
    }

    public void setKennellingCharacteristic(String kennellingCharacteristic) {
        this.kennellingCharacteristic = kennellingCharacteristic;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }
}
