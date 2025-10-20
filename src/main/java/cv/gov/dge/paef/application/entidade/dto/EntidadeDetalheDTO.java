// src/main/java/cv/gov/dge/paef/interfaces/dto/EntidadeDetalheDTO.java
package cv.gov.dge.paef.application.entidade.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record EntidadeDetalheDTO(
        String denominacao,
        BigDecimal nif,
        String natureza,
        String email,
        String url,
        @JsonProperty("geog_local_id") String geogLocalId,
        List<AcreditacaoDTO> acreditacoes
) {
    @Builder
    public record AcreditacaoDTO(
            @JsonProperty("codigo_familia") String codigoFamilia,
            @JsonProperty("denominacao_familia") String denominacaoFamilia,
            String nivel,
            @JsonProperty("self") String self,
            String versao,
            @JsonProperty("codigo_qualificacao") String codigoQualificacao,
            @JsonProperty("denominacao_qualificacao") String denominacaoQualificacao,
            String modalidade,
            String metodologia,
            @JsonProperty("nr_alvara") String nrAlvara,
            @JsonProperty("id_alvara") String idAlvara,
            @JsonProperty("status_alvara") String statusAlvara,
            @JsonProperty("flag_catalogo") String flagCatalogo,
            String situacao
    ) {}
}
