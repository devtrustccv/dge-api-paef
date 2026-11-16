package cv.gov.dge.paef.interfaces.dto.Entidade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record EntidadeCreateResponseDTO(
        @JsonProperty("id")
        String id,

        @JsonProperty("denominacao_social")
        String denominacaoSocial,

        @JsonProperty("nif")
        String nif
) {}
