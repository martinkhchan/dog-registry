package com.polarissoftware.dogregistry.mapper;

import com.polarissoftware.dogregistry.dto.DogDto;
import com.polarissoftware.dogregistry.model.DogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta")
public interface DogMapper {

    DogEntity toEntity(DogDto dto);

    DogDto toDto(DogEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    void updateEntityFromDto(DogDto dto, @MappingTarget DogEntity entity);
}
