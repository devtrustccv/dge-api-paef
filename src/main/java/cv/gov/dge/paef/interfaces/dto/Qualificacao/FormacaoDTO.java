package cv.gov.dge.paef.interfaces.dto.Qualificacao;

import lombok.Builder;

@Builder
public record FormacaoDTO(
        String familia,
        String nivel,
        String qualificacaoFormacao,
        String tipo,
        String metodologia
) {}
