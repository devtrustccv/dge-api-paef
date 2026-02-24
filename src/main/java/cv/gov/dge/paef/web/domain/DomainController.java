package cv.gov.dge.paef.web.domain;

import cv.gov.dge.paef.application.domain.service.DominioService;
import cv.gov.dge.paef.interfaces.dto.DominioKeyDTO;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/domain")
public class DomainController {

    private final DominioService service;

    public DomainController(DominioService service) {
        this.service = service;
    }

    @GetMapping("/{dominio}")
    public org.springframework.http.ResponseEntity<EnvelopeData<java.util.List<OptionDTO>>> list(
            @PathVariable String dominio
    ) {
        return ResponseEntity.ok(new EnvelopeData<>(service.list(dominio)));
    }

    @GetMapping("/{dominio}/{key}")
    public org.springframework.http.ResponseEntity<EnvelopeData<DominioKeyDTO>> getDesc(
            @PathVariable String dominio,
            @PathVariable String key
    ) {
        return ResponseEntity.ok(new EnvelopeData<>(service.getDesc(dominio, key)));
    }
}
