package cv.gov.dge.paef.interfaces.dto.Entidade;

import lombok.Builder;

@Builder
public record EntidadeContaDTO(
        String nif,
        String denominacao,
        String flagMaster
) {}
