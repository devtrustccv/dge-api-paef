package cv.gov.dge.paef.application.AreaQualif.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada/saída para criação de área/qualificação.
 * Campos extras como denominacaoFamilia são aceitos mas não persistidos nesta tabela.
 */
public record AreaQualifDTO(
        @NotBlank @Size(max = 255) String codigoQualif,
        String selfId,
        @NotBlank @Size(max = 1000) String denominacaoQualif,
        @NotBlank String versao,
        String nivel,
        String codigoFamilia,
        String denominacaoFamilia // não persiste aqui
) { }
