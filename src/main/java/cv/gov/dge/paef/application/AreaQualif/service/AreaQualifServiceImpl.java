package cv.gov.dge.paef.application.AreaQualif.service;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.domain.areaqualif.business.AreaQualifBus;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.mapper.AreaQualifMapper;
import cv.gov.dge.paef.infrastructure.repository.AreaQualifRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // upsert simples: como PK = id, save() resolve insert/update
        AreaQualifEntity entity = mapper.toEntity(model);
        AreaQualifEntity saved = bus.save(entity);

        return mapper.toModel(saved);
    }
}
