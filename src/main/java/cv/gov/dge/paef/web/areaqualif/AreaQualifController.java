package cv.gov.dge.paef.web.areaqualif;

import cv.gov.dge.paef.application.AreaQualif.service.AreaQualifService;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.domain.areaqualif.business.AreaQualifBus;
import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/qualificacoes")
@RequiredArgsConstructor
public class AreaQualifController {

    private final AreaQualifService service;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse<AreaQualifDTO>> create(@Valid @RequestBody AreaQualifDTO dto) {
        if (dto.codigoQualif() != null && dto.versao() != null) {
            var existing = service.findExisting(dto.codigoQualif(), dto.versao());
            if (existing) {
                var data = new HashMap<String,Object>();
                data.put("Qualificacao", dto.codigoQualif());
                data.put("Familia", dto.codigoFamilia());
                data.put("alreadyExisted", true);

                // 200 OK + success=false
                return ResponseEntity.ok(
                        ApiResponse.ok("Qualificação já existe (código + versão). Não foi registada.", dto)
                );

                // Se preferir 409 Conflict, use:
                // return ResponseEntity.status(409)
                //     .body(ApiResponse.fail("Qualificação já existe (código + versão). Não foi registada.", data));
            }
        }
        AreaQualif saved = service.createOrUpdate(dto);
        return ResponseEntity
                .created(null)
                .body(ApiResponse.ok("Qualificação registada com sucesso", dto));
    }
}
