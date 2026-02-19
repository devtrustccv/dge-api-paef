package cv.gov.dge.paef.web.alvara;

import cv.gov.dge.paef.application.alvara.dto.AlvaraDTO;
import cv.gov.dge.paef.application.alvara.service.AlvaraService;
import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.infrastructure.mapper.AlvaraMapper;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/alvaras")
public class AlvaraController {
    private final AlvaraService service; private final AlvaraMapper mapper;
    public AlvaraController(AlvaraService s, AlvaraMapper m){ this.service=s; this.mapper=m; }

    @PostMapping
    public ResponseEntity<AlvaraEntity> create(@Valid @RequestBody AlvaraDTO dto){
        var saved = service.save(mapper.toEntity(dto));
        return ResponseEntity.created(URI.create("/api/alvaras/" + saved.getId())).body(saved);
    }
    @GetMapping("/{nif}")
    public ResponseEntity<EnvelopeData<List<OptionDTO>>> alvarasByEntidade(@PathVariable Long nif) {
        var out = service.listOptionsByEntidadeNif(nif);
        return ResponseEntity.ok(new EnvelopeData<>(out));
    }
}
