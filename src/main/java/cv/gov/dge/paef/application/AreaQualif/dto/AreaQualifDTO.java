package cv.gov.dge.paef.application.AreaQualif.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada/saída para criação de área/qualificação.
 * Campos extras como denominacaoFamilia são aceitos mas não persistidos nesta tabela.
 */
@Getter
@Setter
public class AreaQualifDTO{
    @NotBlank @Size(max = 255) @JsonProperty("codigoQualif")
    String codigoQualif;
    @JsonProperty("selfId")
    String selfId;
    @NotBlank @Size(max = 1000)  @JsonProperty("denominacaoQualif")
    String denominacaoQualif;
    @NotBlank String versao;
    String nivel;
    @JsonProperty("codigoFamilia")
    String codigoFamilia;
    @JsonProperty("denominacaoFamilia")
    String denominacaoFamilia;
    @JsonProperty("tipoIntegracao")
    String tipoIntegraca;
}
