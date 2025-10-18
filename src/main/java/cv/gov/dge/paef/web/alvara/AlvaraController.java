package cv.gov.dge.paef.interfaces.rest;

import cv.gov.dge.paef.application.alvara.dto.AlvaraDTO;
import cv.gov.dge.paef.application.alvara.service.AlvaraService;
import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.infrastructure.mapper.AlvaraMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController @RequestMapping("/alvaras")
public class AlvaraController {
    private final AlvaraService service; private final AlvaraMapper mapper;
    public AlvaraController(AlvaraService s, AlvaraMapper m){ this.service=s; this.mapper=m; }

    @PostMapping
    public ResponseEntity<AlvaraEntity> create(@Valid @RequestBody AlvaraDTO dto){
        var saved = service.save(mapper.toEntity(dto));
        return ResponseEntity.created(URI.create("/api/alvaras/" + saved.getId())).body(saved);
    }
}
