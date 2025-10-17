package cv.gov.dge.paef.web.areaqualif;

import cv.gov.dge.paef.application.AreaQualif.service.AreaQualifService;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.domain.areaqualif.business.AreaQualifBus;
import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/qualificacoes")
@RequiredArgsConstructor
public class AreaQualifController {

    private final AreaQualifService service;


    @PostMapping("/save")
    public ResponseEntity<AreaQualif> create(@Valid @RequestBody AreaQualifDTO dto) {
        AreaQualif saved = service.createOrUpdate(dto);
        return ResponseEntity
                .created(URI.create("/api/qualificacoes/save/" + saved.getId()))
                .body(saved);
    }
}
