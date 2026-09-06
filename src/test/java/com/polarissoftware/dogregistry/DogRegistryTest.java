package com.polarissoftware.dogregistry;


import com.polarissoftware.dogregistry.dto.DogDto;
import com.polarissoftware.dogregistry.model.DogEntity;
import com.polarissoftware.dogregistry.model.DogStatus;
import com.polarissoftware.dogregistry.model.Gender;
import com.polarissoftware.dogregistry.model.LeavingReason;
import com.polarissoftware.dogregistry.repository.DogRepository;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
class DogRegistryTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    DogRepository dogRepository;

    @BeforeEach
    void setup() {
        // Clears the in-memory database between test cases
        dogRepository.deleteAll();
    }

    @Test
    void testItWorks() {
        assertTrue(application.isRunning());
    }

    @Test
    void testListWithFilterFlow() {
        // Insert distinct test data into the database
        DogEntity dog1 = new DogEntity();
        dog1.setName("Buddy");
        dog1.setBreed("Labrador");
        dog1.setSupplier("Oxford Kennels");
        dog1.setBadgeId("B001");
        dog1.setGender(Gender.MALE);
        dog1.setBirthDate(LocalDate.of(2021, 4, 10));
        dog1.setDateAcquired(LocalDate.of(2023, 9, 5));
        dog1.setCurrentStatus(DogStatus.IN_SERVICE);
        dog1.setDeleted(false);
        dogRepository.save(dog1);

        DogEntity dog2 = new DogEntity();
        dog2.setName("Max");
        dog2.setBreed("Boxer");
        dog2.setSupplier("Thames Valley Dogs");
        dog2.setBadgeId("B002");
        dog2.setGender(Gender.FEMALE);
        dog2.setBirthDate(LocalDate.of(2023, 9, 28));
        dog2.setDateAcquired(LocalDate.of(2026, 7, 5));
        dog2.setCurrentStatus(DogStatus.IN_TRAINING);
        dog2.setDeleted(false);
        dogRepository.save(dog2);

        // URL encode the JSON payload filter string
        String rawFilter = "{\"breed\":\"Lab\"}";
        String encodedFilter = URLEncoder.encode(rawFilter, StandardCharsets.UTF_8);

        // GET /api/dogs/dogs?filter={"breed":"Lab"}
        Page<?> resultPage = client.toBlocking()
                .retrieve(HttpRequest.GET("/api/dogs/dogs?filter=" + encodedFilter), Page.class);

        // Confirm the filter cleanly narrowed the results down to just the Labrador
        assertEquals(1, resultPage.getContent().size());
    }

    @Test
    void testCreateFlow() {
        // Call the create endpoint.
        DogDto dogDto = new DogDto(null, "Buddy", "Labrador", "Oxford Kennels", "B001", Gender.MALE,
                LocalDate.of(2021, 4, 10), LocalDate.of(2023, 9, 5), DogStatus.IN_SERVICE,
                null, null, null, null, null);
        HttpResponse<DogDto> response = client.toBlocking().exchange(HttpRequest.POST("/api/dogs", dogDto), DogDto.class);

        // Verify response.
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertNotNull(response.body());

        // Get entity from repository.
        Optional<DogEntity> optionalDogEntity = dogRepository.findByIdAndDeletedFalse(response.body().id());

        // Verify the entity exists and is active (deleted = false).
        assertTrue(optionalDogEntity.isPresent(), "The dog record should be in the database and active.");
        // Verify fields are persisted correctly: check Dto <-> Mapper <-> Entity
        DogEntity dogEntity = optionalDogEntity.get();
        assertEquals(dogDto.name(), dogEntity.getName(), "Name should be saved correctly.");
        assertEquals(dogDto.breed(), dogEntity.getBreed(), "Breed should be saved correctly.");
        assertEquals(dogDto.supplier(), dogEntity.getSupplier(), "Supplier should be saved correctly.");
        assertEquals(dogDto.badgeId(), dogEntity.getBadgeId(), "Badge Id should be saved correctly.");
        assertEquals(dogDto.gender(), dogEntity.getGender(), "Gender should be saved correctly.");
        assertEquals(dogDto.birthDate(), dogEntity.getBirthDate(), "Birth Date should be saved correctly.");
        assertEquals(dogDto.dateAcquired(), dogEntity.getDateAcquired(), "Date Acquired should be saved correctly.");
        assertEquals(dogDto.currentStatus(), dogEntity.getCurrentStatus(), "Current Status should be saved correctly.");
        assertEquals(dogDto.leavingDate(), dogEntity.getLeavingDate(), "Leaving Date should be saved correctly.");
        assertEquals(dogDto.leavingReason(), dogEntity.getLeavingReason(), "Date Reason should be saved correctly.");
        assertEquals(dogDto.kennellingCharacteristic(), dogEntity.getKennellingCharacteristic(), "Kennelling Characteristic should be saved correctly.");
    }

    @Test
    void testUpdateFlow() {
        // Create and save an active dog directly to the database
        DogEntity dogEntity = new DogEntity();
        dogEntity.setName("Buddy");
        dogEntity.setBreed("Labrador");
        dogEntity.setSupplier("Oxford Kennels");
        dogEntity.setBadgeId("B001");
        dogEntity.setGender(Gender.MALE);
        dogEntity.setBirthDate(LocalDate.of(2021, 4, 10));
        dogEntity.setDateAcquired(LocalDate.of(2023, 9, 5));
        dogEntity.setCurrentStatus(DogStatus.IN_SERVICE);
        dogEntity.setDeleted(false);
        dogEntity = dogRepository.save(dogEntity);

        // Call the update endpoint with modified payload.
        DogDto updateDto = new DogDto(dogEntity.getId(), dogEntity.getName(), dogEntity.getBreed(), dogEntity.getSupplier(),
                dogEntity.getBadgeId(), dogEntity.getGender(), dogEntity.getBirthDate(), dogEntity.getDateAcquired(), DogStatus.LEFT,
                LocalDate.of(2025, 12, 1), LeavingReason.TRANSFERRED, dogEntity.getKennellingCharacteristic(),
                null, null);
        HttpResponse<DogDto> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/dogs/" + dogEntity.getId(), updateDto), DogDto.class
        );

        // Verify response.
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.body());

        // Get entity from repository.
        Optional<DogEntity> optionalDogEntity = dogRepository.findByIdAndDeletedFalse(response.body().id());

        // Verify the entity exists and is active (deleted = false).
        assertTrue(optionalDogEntity.isPresent(), "The dog record should be in the database and active.");
        // Verify fields are updated correctly.
        DogEntity updatedEntity = optionalDogEntity.get();

        assertEquals(DogStatus.LEFT, updatedEntity.getCurrentStatus(), "Current Status should be updated correctly.");
        assertEquals(LocalDate.of(2025, 12, 1), updatedEntity.getLeavingDate(), "Leaving Date should be updated correctly.");
        assertEquals(LeavingReason.TRANSFERRED, updatedEntity.getLeavingReason(), "Leaving Reason should be updated correctly.");
    }

    @Test
    void testSoftDeleteFlow() {
        // Create and save an active dog directly to the database
        DogEntity dog = new DogEntity();
        dog.setName("Buddy");
        dog.setBreed("Labrador");
        dog.setSupplier("Oxford Kennels");
        dog.setBadgeId("B001");
        dog.setGender(Gender.MALE);
        dog.setBirthDate(LocalDate.of(2021, 4, 10));
        dog.setDateAcquired(LocalDate.of(2023, 9, 5));
        dog.setCurrentStatus(DogStatus.IN_SERVICE);
        dog.setDeleted(false);
        dog = dogRepository.save(dog);

        // Call the DELETE endpoint
        HttpResponse<Void> deleteResponse = client.toBlocking()
                .exchange(HttpRequest.DELETE("/api/dogs/" + dog.getId()), Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());

        // Verify the dog is no longer accessible via a normal GET lookup
        final Long dogId = dog.getId();
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/api/dogs/" + dogId), DogDto.class);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        // Bypassing the controller proves it still physically exists for audit purposes
        Optional<DogEntity> dbCheck = dogRepository.findById(dog.getId());
        assertTrue(dbCheck.isPresent(), "The record should never be deleted from the database!");
        assertTrue(dbCheck.get().isDeleted(), "The record should be flagged as deleted.");
    }

}
