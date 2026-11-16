package cv.gov.dge.paef.web.notificacao;

import cv.gov.dge.paef.application.notificacao.service.NotificacaoRespostaService;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoRespostaRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/notificacoes/{id}")
public class NotificacaoRespostaController {
    private final NotificacaoRespostaService service;

    public NotificacaoRespostaController(NotificacaoRespostaService service) {
        this.service = service;
    }

    @PostMapping(value = "/respostas", consumes = "application/json")
    public ResponseEntity<EnvelopeData<?>> responder(@PathVariable("id") String id,
                                                    @Valid @RequestBody NotificacaoRespostaRequestDTO dto) {
        var resposta = service.responder(id, dto);
        return ResponseEntity.status(201).body(new EnvelopeData<>(ApiResponse.ok(
                "Resposta registada; envio aos emails configurados agendado", resposta)));
    }

    @PostMapping(value = "/respostas", consumes = "multipart/form-data")
    public ResponseEntity<EnvelopeData<?>> responderComAnexos(@PathVariable("id") String id,
            @Valid @RequestPart("resposta") NotificacaoRespostaRequestDTO dto,
            @RequestPart(value = "anexos", required = false) java.util.List<org.springframework.web.multipart.MultipartFile> anexos) {
        var resposta = service.responder(id, dto, anexos);
        return ResponseEntity.status(201).body(new EnvelopeData<>(ApiResponse.ok(
                "Resposta registada; envio aos emails configurados agendado", resposta)));
    }
    @GetMapping("/mensagens")
    public ResponseEntity<EnvelopeData<?>> listar(@PathVariable("id") String id,
                                                 @RequestParam("nif") @NotNull @Positive BigDecimal nif) {
        return ResponseEntity.ok(new EnvelopeData<>(ApiResponse.ok(
                "Mensagens da notificação carregadas com sucesso", service.listar(id, nif))));
    }
}
