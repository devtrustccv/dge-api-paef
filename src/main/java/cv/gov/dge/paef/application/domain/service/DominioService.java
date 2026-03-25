package cv.gov.dge.paef.application.domain.service;

import cv.gov.dge.paef.infrastructure.repository.DomainRepository;
import cv.gov.dge.paef.interfaces.dto.DominioKeyDTO;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DominioService {

    private final DomainRepository repo;

    public DominioService(DomainRepository repo) {
        this.repo = repo;
    }

    public java.util.List<OptionDTO> list(String dominio) {
        return repo.findByDominio(dominio).stream()
                .map(r -> OptionDTO.builder().key(r.key()).value(r.value()).build())
                .toList();
    }

    public DominioKeyDTO getDesc(String dominio, String key) {
        String desc = repo.findDescByDominioAndValor(dominio, key);
        if (desc == null) desc = "";
        return DominioKeyDTO.builder()
                .dominio(dominio)
                .key(key)
                .description(desc)
                .build();
    }
}
