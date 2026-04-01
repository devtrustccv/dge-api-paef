package cv.gov.dge.paef.application.AreaQualif.service;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.repository.AreaQualifRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public interface AreaQualifService {
    AreaQualif createOrUpdate(AreaQualifDTO dto);
    boolean findExisting(String codigo, String versao);
   AreaQualif revoke(AreaQualifDTO dto);
    Map<String, Integer> processQualificacoes(List<AreaQualifDTO> dtos);

}
