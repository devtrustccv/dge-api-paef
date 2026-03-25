package cv.gov.dge.paef.web.formacao;

import cv.gov.dge.paef.application.formacao.service.FormacaoService;
import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.infrastructure.mapper.FormacaoMapper;
import cv.gov.dge.paef.application.formacao.dto.FormacaoDTO;
import cv.gov.dge.paef.interfaces.dto.Entidade.RvccEntidadeDTO;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoOutroDto;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoOutroFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/formacoes")
public class FormacaoController {
    private final FormacaoService service; private final FormacaoMapper mapper;
    public FormacaoController(FormacaoService s, FormacaoMapper m){ this.service=s; this.mapper=m; }

    @PostMapping
    public ResponseEntity<FormacaoEntity> create(@Valid @RequestBody FormacaoDTO dto){
        var saved = service.save(mapper.toEntity(dto));
        return ResponseEntity.created(URI.create("/api/formacoes/" + saved.getId())).body(saved);
    }
    @GetMapping("/outro")
    public ResponseEntity<EnvelopeData<List<FormacaoOutroDto>>> listarFormacoesOutro(FormacaoOutroFilter filter) {
        System.out.println(filter.getFamilia());
        List<FormacaoOutroDto> data = service.listarFormacoesOutro(filter);

        return ResponseEntity.ok(new EnvelopeData<>(data));
    }
    @GetMapping("/qualificacoes")
    public ResponseEntity<EnvelopeData<List<RvccEntidadeDTO>>> listarQualificacoesRvcc() {
        return ResponseEntity.ok(service.listarQualificacoesRvcc());
    }
    @PostMapping("/qualificacoes/mark-sent")
    public ResponseEntity<EnvelopeData<?>> markSent(
            @RequestBody @NotEmpty List<@NotBlank String> ids
    ) {
        return ResponseEntity.ok(new EnvelopeData<>(service.markSent(ids)));
    }
}
