package cv.gov.dge.paef.application.alvara.service;

import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.infrastructure.repository.AlvaraRepository;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service @Transactional
public class AlvaraService {
    private final AlvaraRepository repo;
    public AlvaraService(AlvaraRepository repo){ this.repo = repo; }
    public AlvaraEntity save(AlvaraEntity e){ return repo.save(e); }
    public List<OptionDTO> listOptionsByEntidadeNif(Long nif) {
        var rows = repo.findAlvarasOptionsByNif(new BigDecimal(nif));
        return rows.stream()
                .map(r -> OptionDTO.builder()
                        .key(r.getKey())
                        .value(r.getValue())
                        .build())
                .toList();
    }
}
