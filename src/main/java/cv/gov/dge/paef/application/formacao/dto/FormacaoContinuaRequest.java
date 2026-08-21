package cv.gov.dge.paef.application.formacao.dto;

import jakarta.validation.constraints.NotBlank;

public record FormacaoContinuaRequest(
        @NotBlank String idAlvara,
        @NotBlank String idAreaQualif,
        @NotBlank String idQualificacao,
        String areaDesc,
        String qualificacaoDesc,
        @NotBlank String dmNivelRomano,
        @NotBlank String dmMetodologia
) {}
