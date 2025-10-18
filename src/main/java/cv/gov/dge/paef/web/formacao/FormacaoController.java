package cv.gov.dge.paef.web.formacao;

import cv.gov.dge.paef.application.formacao.service.FormacaoService;
import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.infrastructure.mapper.FormacaoMapper;
import cv.gov.dge.paef.interfaces.dto.FormacaoDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController @RequestMapping("/formacoes")
public class FormacaoController {
    private final FormacaoService service; private final FormacaoMapper mapper;
    public FormacaoController(FormacaoService s, FormacaoMapper m){ this.service=s; this.mapper=m; }

    @PostMapping
    public ResponseEntity<FormacaoEntity> create(@Valid @RequestBody FormacaoDTO dto){
        var saved = service.save(mapper.toEntity(dto));
        return ResponseEntity.created(URI.create("/api/formacoes/" + saved.getId())).body(saved);
    }
}
