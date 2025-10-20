package cv.gov.dge.paef.web.entidade;

import cv.gov.dge.paef.application.entidade.dto.EntidadeDetalheDTO;
import cv.gov.dge.paef.application.entidade.dto.EntidadePffpDTO;
import cv.gov.dge.paef.application.entidade.service.EntidadeQueryService;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.PffpMarkSentResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/entidades")
public class EntidadeQueryController {

    private final EntidadeQueryService service;

    public EntidadeQueryController(EntidadeQueryService service) { this.service = service; }

    @GetMapping("/{nif}")
    public ResponseEntity<EnvelopeData<EntidadeDetalheDTO>> getByNif(@PathVariable BigDecimal nif) {
        return ResponseEntity.ok(new EnvelopeData<>(service.getDetalheByNif(nif)));
    }

    /** Lista todas as entidades com alvará não enviado ao SGF (sended_to_sgf != true). */
    @GetMapping("/pendentes-sgf")
    public ResponseEntity<EnvelopeData<List<EntidadeDetalheDTO>>> listPendentesSgf() {
        var lista = service.listEntidadesPendentesSgf();
        return ResponseEntity.ok(new EnvelopeData<>(lista));
    }

    @GetMapping("/pendentes-pffp")
    public ResponseEntity<EnvelopeData<List<EntidadePffpDTO>>> listPendentesPffp() {
        var lista = service.listEntidadesPendentesPFFP();
        return ResponseEntity.ok(new EnvelopeData<>(lista));
    }

    @PostMapping("/pendentes-pffp/mark-sent")
    public ResponseEntity<EnvelopeData<List<PffpMarkSentResultDTO>>> markSentByNif(
            @RequestBody List<Long> nifs) {

        var results = service.markManyByNifs(nifs);
        return ResponseEntity.ok(new EnvelopeData<>(results));
    }
}
