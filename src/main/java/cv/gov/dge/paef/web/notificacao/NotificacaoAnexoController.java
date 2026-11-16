package cv.gov.dge.paef.web.notificacao;

import cv.gov.dge.paef.application.notificacao.service.NotificacaoAnexoService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/notificacoes/{id}/anexos")
public class NotificacaoAnexoController {
    private final NotificacaoAnexoService service;
    public NotificacaoAnexoController(NotificacaoAnexoService service) { this.service = service; }

    @GetMapping("/{idDocumento}")
    public ResponseEntity<byte[]> descarregar(@PathVariable("id") String id,
                                             @PathVariable("idDocumento") String idDocumento,
                                             @RequestParam("nif") BigDecimal nif) {
        var doc = service.obter(id, idDocumento, nif);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getContentType() == null
                        ? "application/octet-stream" : doc.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(doc.getDescricao() == null ? "anexo" : doc.getDescricao(), StandardCharsets.UTF_8)
                        .build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(doc.getFile());
    }
}