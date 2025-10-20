// src/main/java/cv/gov/dge/paef/interfaces/dto/PffpMarkSentResultDTO.java
package cv.gov.dge.paef.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PffpMarkSentResultDTO(
        Long nif,
        boolean updated,
        String message
) {}


