package cv.gov.dge.paef.interfaces.dto.logs;

import lombok.Builder;

@Builder
public record HistoricoDTO(
        String dataUser,        // "dd-mm-yyyy/username"
        String estado,          // campoAlt
        String valorAnterior,
        String valorAtual
) {}
