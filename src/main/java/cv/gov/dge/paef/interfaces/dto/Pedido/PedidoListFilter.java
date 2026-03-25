package cv.gov.dge.paef.interfaces.dto.Pedido;

public record PedidoListFilter(
        String estado,
        String tipoPedido,
        String periodo,
        Integer page,
        Integer size
) {}
