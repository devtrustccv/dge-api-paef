package cv.gov.dge.paef.web.pedido;
import cv.gov.dge.paef.application.pedido.service.PedidoService;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.Pedido.PedidoListFilter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<EnvelopeData<?>> listar(
            @RequestParam("nif_entidade")
            @NotNull @Positive BigDecimal nifEntidade,

            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "tipoPedido", required = false) String tipoPedido,
            @RequestParam(value = "periodo", required = false) String periodo,

            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        var filter = new PedidoListFilter(estado, tipoPedido, periodo, page, size);
        return ResponseEntity.ok(new EnvelopeData<>(service.listar(nifEntidade, filter)));
    }
}