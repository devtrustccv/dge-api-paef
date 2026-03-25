package cv.gov.dge.paef.web.proconline;

import cv.gov.dge.paef.application.proconline.service.ProcOnlineService;
import cv.gov.dge.paef.infrastructure.mapper.ProcOnlineMapper;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.ProcOnlineDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// interfaces/rest/ProcOnlineController.java
@RestController
@RequestMapping("/proc-online")
public class ProcOnlineController {

    private final ProcOnlineService service;
    private final ProcOnlineMapper mapper;

    public ProcOnlineController(ProcOnlineService service, ProcOnlineMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<EnvelopeData<List<ProcOnlineDTO>>> list(
            @RequestParam(required = false) String nif,
            @RequestParam(required = false) String idEntidade,
            @RequestParam(required = false) String token
    ) {
        var out = service.list(nif).stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(new EnvelopeData<>(out));
    }
}

