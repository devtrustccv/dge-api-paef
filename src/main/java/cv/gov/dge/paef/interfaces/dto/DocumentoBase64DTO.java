package cv.gov.dge.paef.interfaces.dto;

import lombok.Builder;

@Builder
public record DocumentoBase64DTO(
        String mimeType,
        String data
) {}
