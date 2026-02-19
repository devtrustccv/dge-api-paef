// src/main/java/cv/gov/dge/paef/interfaces/rest/GeografiaController.java
package cv.gov.dge.paef.interfaces.rest;

import cv.gov.dge.paef.domain.geografia.service.GeografiaService;
import cv.gov.dge.paef.infrastructure.mapper.GeografiaMapper;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.GeografiaOptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geografia")
public class GeografiaController {

    private final GeografiaService service;
    private final GeografiaMapper mapper;

    public GeografiaController(GeografiaService service, GeografiaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/ilhas")
    public ResponseEntity<EnvelopeData<List<GeografiaOptionDTO>>> ilhas() {
        var out = service.ilhas().stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(new EnvelopeData<>(out));
    }

    @GetMapping("/concelhos")
    public ResponseEntity<EnvelopeData<List<GeografiaOptionDTO>>> concelhos(@RequestParam String ilha) {
        var out = service.concelhos(ilha).stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(new EnvelopeData<>(out));
    }

    @GetMapping("/freguesias")
    public ResponseEntity<EnvelopeData<List<GeografiaOptionDTO>>> freguesias(@RequestParam String concelho) {
        var out = service.freguesias(concelho).stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(new EnvelopeData<>(out));
    }

    @GetMapping("/zonas")
    public ResponseEntity<EnvelopeData<List<GeografiaOptionDTO>>> zonas(@RequestParam String freguesia) {
        var out = service.zonas(freguesia).stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(new EnvelopeData<>(out));
    }
}
