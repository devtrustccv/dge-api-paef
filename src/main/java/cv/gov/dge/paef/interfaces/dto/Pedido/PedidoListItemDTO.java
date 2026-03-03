package cv.gov.dge.paef.interfaces.dto.Pedido;

import lombok.Builder;

@Builder
public record PedidoListItemDTO(
        String nProcesso,
        String tipoPedidoLis,
        String utilizadorRegisto,
        String dataPedido,
        String dataFim,
        String estadoEtapa,
        String resultado,
        String idProcesso,
        String idTask,
        String etapaAtual,
        String idPedido
) {}
