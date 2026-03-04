package cv.gov.dge.paef.web.documento;

import cv.gov.dge.paef.application.documento.service.DocumentoService;
import cv.gov.dge.paef.infrastructure.repository.ClobRepository;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService service;
    private final ClobRepository clobRepository;

    public DocumentoController(DocumentoService service, ClobRepository clobRepo) {
        this.service = service;
        this.clobRepository = clobRepo;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EnvelopeData<?>> get(@PathVariable String uuid) {
        return ResponseEntity.ok(new EnvelopeData<>(service.getByUuid(uuid)));
    }

    @GetMapping("/ver/{uuid}")
    public ResponseEntity<byte[]> view(@PathVariable String uuid) {
        return service.visualizarNoBrowser(uuid);
    }
}