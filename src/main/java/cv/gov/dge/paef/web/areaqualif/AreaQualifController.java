package cv.gov.dge.paef.web.areaqualif;

import cv.gov.dge.paef.application.AreaQualif.service.AreaQualifService;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.domain.areaqualif.business.AreaQualifBus;
import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/qualificacoes")
@RequiredArgsConstructor
public class AreaQualifController {

    private final AreaQualifService service;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody EnvelopeData<List<AreaQualifDTO>> datas
    ) {
        try {
            var result = service.processQualificacoes(datas.getData());
            return ResponseEntity.ok(
                    ApiResponse.ok("Qualificações processadas com sucesso", result)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail(
                            "Erro ao consumir o endpoint: " + e.getMessage(), datas));
        }
    }
}
