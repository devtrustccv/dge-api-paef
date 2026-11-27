package cv.gov.dge.paef.web.pedido;

import cv.gov.dge.paef.application.pedido.service.PedidoService;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.Pedido.PedidoListItemDTO;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/processos_pendentes")
public class ProcessosPendentesController {
    private final PedidoService service;

    public ProcessosPendentesController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<EnvelopeData<ApiResponse<List<PedidoListItemDTO>>>> listar(
            @RequestParam("nif_entidade") @Positive BigDecimal nifEntidade) {
        return ResponseEntity.ok(new EnvelopeData<>(service.listarProcessosPendentes(nifEntidade)));
    }
}
