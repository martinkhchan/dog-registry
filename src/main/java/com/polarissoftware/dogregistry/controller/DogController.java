package com.polarissoftware.dogregistry.controller;

import com.polarissoftware.dogregistry.dto.DogDto;
import com.polarissoftware.dogregistry.dto.DogFilter;
import com.polarissoftware.dogregistry.mapper.DogMapper;
import com.polarissoftware.dogregistry.model.DogEntity;
import com.polarissoftware.dogregistry.repository.DogRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.json.JsonMapper;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;

import java.io.IOException;

@Validated
@Controller("/api/dogs")
public class DogController {

    private final DogRepository dogRepository;
    private final DogMapper dogMapper;
    private final JsonMapper jsonMapper;

    public DogController(DogRepository dogRepository, DogMapper dogMapper, JsonMapper jsonMapper) {
        this.dogRepository = dogRepository;
        this.dogMapper = dogMapper;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Create a new dog record.
     */
    @Post
    public HttpResponse<DogDto> createDog(@Body @Valid DogDto dto) {
        DogEntity entity = dogMapper.toEntity(dto);
        DogEntity saved = dogRepository.save(entity);
        return HttpResponse.created(dogMapper.toDto(saved));
    }

    /**
     * List active dogs.
     */
    @Get("/dogs")
    public HttpResponse<Page<DogDto>> listDogs(@QueryValue @Nullable String filter,
                                               @QueryValue(defaultValue = "0") int page,
                                               @QueryValue(defaultValue = "10") int size) {

        Pageable pageable = Pageable.from(page, size);
        DogFilter dogFilter = parseFilter(filter);

        Page<DogEntity> filteredResults =
                dogRepository.findByDeletedFalseAndNameContainsIgnoreCaseAndBreedContainsIgnoreCaseAndSupplierContainsIgnoreCase(
                        dogFilter.name(), dogFilter.breed(), dogFilter.supplier(), pageable);

        return HttpResponse.ok(filteredResults.map(dogMapper::toDto));
    }

    /**
     * Get a single active dog record by its unique ID.
     */
    @Get("/{id}")
    public HttpResponse<DogDto> getDogById(@PathVariable("id") Long id) {
        return dogRepository.findByIdAndDeletedFalse(id)
                .map(entity -> HttpResponse.ok(dogMapper.toDto(entity)))
                .orElse(HttpResponse.notFound());
    }

    /**
     * Updates an existing active dog record completely.
     */
    @Put("/{id}")
    public HttpResponse<DogDto> updateDog(@PathVariable("id") Long id, @Body @Valid DogDto updateDto) {
        return dogRepository.findByIdAndDeletedFalse(id).map(existingEntity -> {
            dogMapper.updateEntityFromDto(updateDto, existingEntity);
            DogEntity savedEntity = dogRepository.update(existingEntity);
            return HttpResponse.ok(dogMapper.toDto(savedEntity));
        }).orElse(HttpResponse.notFound());
    }

    /**
     * Soft delete a dog record.
     */
    @Delete("/{id}")
    public HttpResponse<Void> softDeleteDog(@PathVariable("id") Long id) {
        return dogRepository.findByIdAndDeletedFalse(id).map(dog -> {
            dog.setDeleted(true);
            dogRepository.update(dog);
            return HttpResponse.<Void>noContent();
        }).orElse(HttpResponse.notFound());
    }

    private DogFilter parseFilter(String filter) {
        if (filter == null || filter.isBlank()) {
            return new DogFilter(null, null, null);
        }
        try {
            return jsonMapper.readValue(filter, DogFilter.class);
        } catch (IOException e) {
            // Handle malformed string input quietly
            return new DogFilter(null, null, null);
        }
    }
}
