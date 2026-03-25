package cv.gov.dge.paef.interfaces.dto.Qualificacao;

import lombok.Builder;

@Builder
public record RvccQualificacaoDTO(
        String codigoCnq,
        String selfidQp,
        String denominacao,
        String familia,
        String codigoFamilia,
        String nivelQnq,
        String idFormacao
) {}
