package cv.gov.dge.paef.application.AreaQualif.service;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.repository.AreaQualifRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface AreaQualifService {
    AreaQualif createOrUpdate(AreaQualifDTO dto);
}
