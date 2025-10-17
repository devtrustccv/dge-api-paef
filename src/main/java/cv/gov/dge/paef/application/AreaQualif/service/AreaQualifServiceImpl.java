package cv.gov.dge.paef.application.AreaQualif.service;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.domain.areaqualif.business.AreaQualifBus;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.domain.areaqualif.model.DuplicateQualificationException;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.mapper.AreaQualifMapper;
import cv.gov.dge.paef.infrastructure.repository.AreaQualifRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AreaQualifServiceImpl implements AreaQualifService {

    private final AreaQualifBus bus;
    private final AreaQualifMapper mapper;

    /**
     * Cria/atualiza registro da paef.paef_t_area_qualif.
     * Regras:
     * - estado default 'A'
     * - id = codigoQualif
     */
    @Transactional
    public AreaQualif createOrUpdate(AreaQualifDTO dto) {
        AreaQualif model = mapper.toModel(dto);

        // defaults / normalização
        if (model.getEstado() == null || model.getEstado().isBlank()) {
            model.setEstado("A");
        }
        // id já mapeado para codigoQualif; garante sigla
        if (model.getSiglaCodigo() == null || model.getSiglaCodigo().isBlank()) {
            model.setSiglaCodigo(model.getSiglaCodigo());
        }
        String familiaCod = dto.codigoFamilia();
        String familiaDesc = dto.denominacaoFamilia();
        String familiaId = null;

        if (familiaCod != null && !familiaCod.isBlank()) {
            AreaQualifEntity familia = bus.findOrCreateFamily(familiaCod, familiaDesc);
            familiaId = familia.getId();
        }
        if (familiaId != null) {
            model.setIdPai(familiaId);
        }
        // upsert simples: como PK = id, save() resolve insert/update
        AreaQualifEntity entity = mapper.toEntity(model);
        AreaQualifEntity saved = bus.save(entity);

        return mapper.toModel(saved);
    }

    public boolean findExisting(String codigo, String versao){
        return bus.existsQualification(codigo, versao);
    }
}
