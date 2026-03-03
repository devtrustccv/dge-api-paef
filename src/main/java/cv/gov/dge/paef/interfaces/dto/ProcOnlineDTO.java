package cv.gov.dge.paef.interfaces.dto;

// interfaces/dto/ProcOnlineRowDTO.java
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ProcOnlineDTO(
        String tipoPedidoProcesso,
        String descricao,
        String linkPage,
        String linkDocHelp
) {}
