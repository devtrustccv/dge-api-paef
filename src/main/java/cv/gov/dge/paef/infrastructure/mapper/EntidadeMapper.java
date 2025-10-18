// src/main/java/cv/gov/dge/paef/interfaces/mapper/EntidadeMapper.java
package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.application.entidade.dto.EntidadeDTO;
import cv.gov.dge.paef.domain.entidade.model.Entidade;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntidadeMapper {
    EntidadeEntity toEntity(EntidadeDTO dto);
    Entidade toModel(EntidadeEntity e);
    EntidadeEntity toEntity(Entidade model);
    EntidadeDTO toDto(EntidadeEntity e);
}
