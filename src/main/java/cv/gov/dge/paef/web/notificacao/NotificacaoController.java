package cv.gov.dge.paef.web.notificacao;

import cv.gov.dge.paef.application.notificacao.service.NotificacaoDetalheService;
import cv.gov.dge.paef.application.notificacao.service.NotificacaoService;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService service;
    private final NotificacaoDetalheService dService;

    public NotificacaoController(NotificacaoService service, NotificacaoDetalheService dService) {
        this.service = service;
        this.dService = dService;
    }

    @GetMapping
    public ResponseEntity<EnvelopeData<?>> listar(
            @RequestParam("nif") @NotNull @Positive BigDecimal nif,
            @RequestParam(value = "alvara", required = false) String alvara,
            @RequestParam(value = "periodo", required = false) String periodo,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        return ResponseEntity.ok(new EnvelopeData<>(service.listar(nif, alvara, periodo, limit)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvelopeData<?>> detalhe(
            @RequestParam("nif") @NotNull @Positive BigDecimal nif,
            @PathVariable("id") String idNotificacao
    ) {
        return ResponseEntity.ok(new EnvelopeData<>(dService.detalhe(nif, idNotificacao)));
    }
}