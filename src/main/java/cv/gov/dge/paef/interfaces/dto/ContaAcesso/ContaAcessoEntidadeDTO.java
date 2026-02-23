package cv.gov.dge.paef.interfaces.dto.ContaAcesso;

// interfaces/dto/ContaAcessoEntidadeDTO.java
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ContaAcessoEntidadeDTO(
        String id,
        String utilizador,
        @JsonProperty("principalMaster") Integer principalMaster,
        @JsonProperty("principalMasterCheck") Integer principalMasterCheck,
        String dataRegisto,
        String estado
) {}
