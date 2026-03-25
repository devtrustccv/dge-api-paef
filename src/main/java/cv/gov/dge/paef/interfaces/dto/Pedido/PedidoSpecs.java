package cv.gov.dge.paef.interfaces.dto.Pedido;

import cv.gov.dge.paef.infrastructure.TPedidoEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class PedidoSpecs {

    private PedidoSpecs() {}

    public static Specification<TPedidoEntity> byIdEntidade(String idEntidade) {
        return (root, q, cb) -> cb.equal(root.get("idEntidade"), idEntidade);
    }

    public static Specification<TPedidoEntity> estadoEquals(String estadoOrNull, String defaultEstado) {
        String estado = (estadoOrNull == null || estadoOrNull.isBlank()) ? defaultEstado : estadoOrNull;
        return (root, q, cb) -> cb.equal(root.get("dmEstadoPedido"), estado);
    }

    public static Specification<TPedidoEntity> tipoPedidoEquals(String tipoPedidoOrNull) {
        if (tipoPedidoOrNull == null || tipoPedidoOrNull.isBlank()) return null;
        return (root, q, cb) -> cb.equal(root.get("idTpProcesso"), tipoPedidoOrNull);
    }

    public static Specification<TPedidoEntity> excludeFiscalizacao() {
        return (root, q, cb) -> cb.notEqual(root.get("idTpProcesso"), "process_Fiscalizacao");
    }

    public static Specification<TPedidoEntity> dtRegistoBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return null;
        return (root, q, cb) -> cb.between(root.get("dtRegisto"), start, end);
    }
}
