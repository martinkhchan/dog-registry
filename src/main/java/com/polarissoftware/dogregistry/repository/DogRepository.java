package com.polarissoftware.dogregistry.repository;

import com.polarissoftware.dogregistry.model.DogEntity;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface DogRepository extends PageableRepository<DogEntity, Long> {

    Optional<DogEntity> findByIdAndDeletedFalse(Long id);

    Page<DogEntity> findByDeletedFalseAndNameContainsIgnoreCaseAndBreedContainsIgnoreCaseAndSupplierContainsIgnoreCase(
            @Nullable String name,
            @Nullable String breed,
            @Nullable String supplier,
            Pageable pageable);
}
