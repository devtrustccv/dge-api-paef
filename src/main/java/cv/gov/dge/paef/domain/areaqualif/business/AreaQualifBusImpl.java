package cv.gov.dge.paef.domain.areaqualif.business;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.application.AreaQualif.service.AreaQualifService;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.mapper.AreaQualifMapper;
import cv.gov.dge.paef.infrastructure.repository.AreaQualifRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AreaQualifBusImpl implements AreaQualifBus {

    private final AreaQualifRepository repo;


    public AreaQualifEntity save(AreaQualifEntity entity) {
        return repo.save(entity);
    }

    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return repo.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsBySiglaCodigo(String sigla) {
        return repo.existsBySiglaCodigo(sigla);
    }


}
