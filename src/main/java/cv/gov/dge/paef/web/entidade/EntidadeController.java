package cv.gov.dge.paef.web.entidade;

import cv.gov.dge.paef.application.entidade.dto.EntidadeDTO;
import cv.gov.dge.paef.domain.entidade.business.EntidadeBus;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeInfoDTO;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;

@RestController
@RequestMapping("/entidades")
public class EntidadeController {

    private final EntidadeBus bus;

    public EntidadeController(EntidadeBus bus){ this.bus = bus; }

    @PostMapping
    public ResponseEntity<EntidadeEntity> create(@Valid @RequestBody EntidadeDTO dto){
        var saved = bus.create(dto);
        return ResponseEntity.created(URI.create("/api/entidades/" + saved.getId())).body(saved);
    }

}
