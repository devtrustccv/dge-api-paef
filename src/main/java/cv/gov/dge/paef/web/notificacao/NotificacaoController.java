package cv.gov.dge.paef.web.notificacao;

import cv.gov.dge.paef.application.notificacao.service.NotificacaoDetalheService;
import cv.gov.dge.paef.application.notificacao.service.NotificacaoService;
import cv.gov.dge.paef.application.notificacao.service.NotificacaoCreateService;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoCreateRequestDTO;
import jakarta.validation.Valid;
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
    private final NotificacaoCreateService createService;

    public NotificacaoController(NotificacaoService service, NotificacaoDetalheService dService,
                                 NotificacaoCreateService createService) {
        this.service = service;
        this.dService = dService;
        this.createService = createService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EnvelopeData<?>> criar(@Valid @RequestBody NotificacaoCreateRequestDTO dto) {
        var criada = createService.criar(dto);
        return ResponseEntity.status(201).body(new EnvelopeData<>(ApiResponse.ok(
                "Notificação registada; envio aos emails configurados agendado", criada)));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<EnvelopeData<?>> criarComAnexos(
            @Valid @RequestPart("notificacao") NotificacaoCreateRequestDTO dto,
            @RequestPart(value = "anexos", required = false) java.util.List<org.springframework.web.multipart.MultipartFile> anexos) {
        var criada = createService.criar(dto, anexos);
        return ResponseEntity.status(201).body(new EnvelopeData<>(ApiResponse.ok(
                "Notificação registada; envio aos emails configurados agendado", criada)));
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
