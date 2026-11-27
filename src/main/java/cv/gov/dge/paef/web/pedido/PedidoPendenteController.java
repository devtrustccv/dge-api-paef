package cv.gov.dge.paef.web.pedido;

import cv.gov.dge.paef.application.pedido.service.PedidoPendenteService;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.Pedido.PedidoPendenteListItemDTO;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/pedidos-pendentes")
public class PedidoPendenteController {
    private final PedidoPendenteService service;

    public PedidoPendenteController(PedidoPendenteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<EnvelopeData<ApiResponse<List<PedidoPendenteListItemDTO>>>> listar(
            @RequestParam("nif_entidade") @Positive BigDecimal nifEntidade) {
        return ResponseEntity.ok(new EnvelopeData<>(service.listar(nifEntidade)));
    }
}
