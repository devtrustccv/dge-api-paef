package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.application.alvara.dto.AlvaraDTO;
import cv.gov.dge.paef.domain.alvara.model.Alvara;
import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlvaraMapper {
    AlvaraEntity toEntity(AlvaraDTO dto);
    Alvara toModel(AlvaraEntity e);
    AlvaraEntity toEntity(Alvara model);
    AlvaraDTO toDto(AlvaraEntity e);
}
