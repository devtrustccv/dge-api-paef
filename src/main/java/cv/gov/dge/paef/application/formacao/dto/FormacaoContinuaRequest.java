package cv.gov.dge.paef.application.formacao.dto;

import jakarta.validation.constraints.NotBlank;

public record FormacaoContinuaRequest(
        @NotBlank String idAlvara,
        @NotBlank String idAreaQualif,
        @NotBlank String idQualificacao,
        String qualificacaoDesc
) {}
