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
    public ResponseEntity<ApiResponse<List<AreaQualifDTO>>> create(@Valid @RequestBody EnvelopeData<List<AreaQualifDTO>> datas) {
        List<AreaQualifDTO> dtos = datas.getData();
        System.out.println("here");
        boolean success = true;
        try {
            for (AreaQualifDTO dto : dtos) {
                if (dto.codigoQualif() != null && dto.versao() != null) {
                    var existing = service.findExisting(dto.codigoQualif(), dto.versao());
                    if (!existing && dto.tipo().equals("PUBLICADO")) {
                        AreaQualif saved = service.createOrUpdate(dto);
                    }else if(dto.tipo().equals("REVOGADO")){

                    }
                }
            }
        }catch (Exception e){
            success=false;
        }
        if(success)
            return ResponseEntity
                .created(null)
                .body(ApiResponse.ok("Qualificações registadas com sucesso", dtos));
        else
            return ResponseEntity
                .created(null)
                .body(ApiResponse.ok("Erro ao consumir o endpoint", null));
    }
}
