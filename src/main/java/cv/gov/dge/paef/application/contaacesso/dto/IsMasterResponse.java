package cv.gov.dge.paef.application.contaacesso.dto;

import lombok.Builder;

@Builder
public record IsMasterResponse(
        boolean isMaster
) {}
