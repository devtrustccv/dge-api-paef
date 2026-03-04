package cv.gov.dge.paef.web.documento;

import cv.gov.dge.paef.application.documento.service.DocumentoService;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService service;

    public DocumentoController(DocumentoService service) {
        this.service = service;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EnvelopeData<?>> get(@PathVariable String uuid) {
        return ResponseEntity.ok(new EnvelopeData<>(service.getByUuid(uuid)));
    }
}