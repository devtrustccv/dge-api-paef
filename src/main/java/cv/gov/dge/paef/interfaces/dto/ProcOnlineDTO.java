package cv.gov.dge.paef.interfaces.dto;

// interfaces/dto/ProcOnlineRowDTO.java
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ProcOnlineDTO(
        @JsonProperty("id_tp_processo") String idTpProcesso,
        @JsonProperty("tipo_pedido__processo") String tipoPedidoProcesso
) {}
