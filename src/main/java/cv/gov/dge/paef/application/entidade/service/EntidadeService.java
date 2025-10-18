package cv.gov.dge.paef.application.entidade.service;

import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service @Transactional
public class EntidadeService {
    private final EntidadeRepository repo;
    public EntidadeService(EntidadeRepository repo){ this.repo = repo; }

    public EntidadeEntity save(EntidadeEntity e){ return repo.save(e); }

    @Transactional(readOnly = true)
    public Optional<EntidadeEntity> findByNif(BigDecimal nif){
        return repo.findByNif(nif);
    }
}
