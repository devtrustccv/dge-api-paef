// src/main/java/cv/gov/dge/paef/interfaces/rest/EntidadeInfoController.java
package cv.gov.dge.paef.interfaces.rest;

import cv.gov.dge.paef.application.entidade.service.EntidadeService;
import cv.gov.dge.paef.domain.entidade.service.EntidadeInfoService;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeUpdateDTO;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeInfoDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/entidade")
public class EntidadeInfoController {

    private final EntidadeInfoService service;
    private final EntidadeService services;

    public EntidadeInfoController(EntidadeInfoService service,
                                  EntidadeService services) {
        this.service = service;
        this.services = services;
    }

    @GetMapping("/{nif}")
    public ResponseEntity<EnvelopeData<EntidadeInfoDTO>> getByNifs(@PathVariable BigDecimal nif) {
        var dto = service.getByNif(nif);
        // se preferires retornar 404 quando não encontra:
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new EnvelopeData<>(dto));
    }

    @PutMapping("/{nif}")
    public ResponseEntity<EnvelopeData<EntidadeInfoDTO>> updateByNif(
            @PathVariable Long nif,
            @Valid @RequestBody EntidadeUpdateDTO body
    ) {
        var out = services.updateByNif(nif, body);
        if (out == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new EnvelopeData<>(out));
    }
}
