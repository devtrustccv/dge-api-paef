// src/main/java/cv/gov/dge/paef/infrastructure/mapper/GeografiaMapper.java
package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.domain.geografia.model.GeografiaOption;
import cv.gov.dge.paef.infrastructure.GeografiaEntity;
import cv.gov.dge.paef.interfaces.dto.GeografiaOptionDTO;
import org.springframework.stereotype.Component;

@Component
public class GeografiaMapper {

    public GeografiaOption toModel(GeografiaEntity e) {
        if (e == null) return null;
        return GeografiaOption.builder()
                .id(e.getId())
                .nome(e.getNome())
                .build();
    }

    public GeografiaOptionDTO toDto(GeografiaOption m) {
        if (m == null) return null;
        return GeografiaOptionDTO.builder()
                .id(m.id())
                .nome(m.nome())
                .build();
    }
}

