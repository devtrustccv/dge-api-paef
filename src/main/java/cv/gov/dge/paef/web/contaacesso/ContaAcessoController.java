package cv.gov.dge.paef.web.contaacesso;

import cv.gov.dge.paef.application.contaacesso.dto.IsMasterResponse;
import cv.gov.dge.paef.application.contaacesso.service.ContaAcessoService;
import cv.gov.dge.paef.interfaces.dto.ContaAcesso.ContaAcessoEntidadeDTO;
import cv.gov.dge.paef.interfaces.dto.ContaAcesso.ToggleMasterRequest;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeContaDTO;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/contas-acesso")
public class ContaAcessoController {

    private final ContaAcessoService service;

    public ContaAcessoController(ContaAcessoService service) {
        this.service = service;
    }

    @GetMapping("/is-master")
    public ResponseEntity<EnvelopeData<IsMasterResponse>> isMaster(
            @RequestParam String emailUser,
            @RequestParam BigDecimal nifEntidade
    ) {

        boolean result = service.isUserMaster(emailUser, nifEntidade);

        return ResponseEntity.ok(
                new EnvelopeData<>(
                        IsMasterResponse.builder()
                                .isMaster(result)
                                .build()
                )
        );
    }

    @GetMapping("/entidades")
    public ResponseEntity<EnvelopeData<List<EntidadeContaDTO>>> listEntidadesByUser(
            @RequestParam String emailUser
    ) {

        var result = service.listEntidades(emailUser);

        return ResponseEntity.ok(new EnvelopeData<>(result));
    }

    @GetMapping("/{nif}")
    public ResponseEntity<EnvelopeData<List<ContaAcessoEntidadeDTO>>> list(@PathVariable Long nif) {
        var out = service.list(nif);
        return ResponseEntity.ok(new EnvelopeData<>(out));
    }

    @PutMapping("/{id}/set-master")
    public ResponseEntity<EnvelopeData<?>> toggleMaster(
            @PathVariable String id,
            @Valid @RequestBody ToggleMasterRequest body
    ) {
        var resp = service.toggleMaster(id, body.nifEntidade(), body.actorEmail());
        return ResponseEntity.ok(new EnvelopeData<>(resp));
    }

    @PutMapping("/{id}/toggle-estado")
    public ResponseEntity<EnvelopeData<?>> toggleEstado(
            @PathVariable String id,
            @RequestParam(required = false) String user_email)
     {
        var resp = service.toggleEstado(id, user_email);
        return ResponseEntity.ok(new EnvelopeData<>(resp));
    }
}
