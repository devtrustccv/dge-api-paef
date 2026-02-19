// src/main/java/cv/gov/dge/paef/interfaces/dto/OptionDTO.java
package cv.gov.dge.paef.interfaces.dto;

import lombok.Builder;

@Builder
public record OptionDTO(
        String key,
        String value
) {}
