package cv.gov.dge.paef.web.entidade;

import cv.gov.dge.paef.domain.entidade.business.EntidadeBus;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeCreateRequestDTO;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeCreateResponseDTO;
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
    public ResponseEntity<EnvelopeData<ApiResponse<EntidadeCreateResponseDTO>>> create(
            @Valid @RequestBody EntidadeCreateRequestDTO dto
    ){
        var result = bus.create(dto);
        var saved = result.entity();
        var body = EntidadeCreateResponseDTO.builder()
                .id(saved.getId())
                .denominacaoSocial(saved.getDenominacaoSocial())
                .nif(saved.getNif() == null ? null : saved.getNif().toPlainString())
                .build();

        var response = new EnvelopeData<>(ApiResponse.ok(
                result.created() ? "Entidade criada com sucesso" : "Entidade j\u00e1 existe para o NIF informado",
                body
        ));

        if (!result.created()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity
                .created(URI.create("/api/entidades/" + saved.getNif()))
                .body(response);
    }

}
