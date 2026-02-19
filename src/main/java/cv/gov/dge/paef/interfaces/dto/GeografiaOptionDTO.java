// src/main/java/cv/gov/dge/paef/interfaces/dto/GeografiaOptionDTO.java
package cv.gov.dge.paef.interfaces.dto;

import lombok.Builder;

@Builder
public record GeografiaOptionDTO(
        String id,
        String nome
) {}
