package cv.gov.dge.paef.domain.areaqualif.business;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.application.AreaQualif.service.AreaQualifService;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.mapper.AreaQualifMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public interface AreaQualifBus {
    AreaQualifEntity save(AreaQualifEntity entity);
    boolean existsById(String id);
    boolean existsBySiglaCodigo(String sigla);
}
