package cv.gov.dge.paef.application.alvara.service;

import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.infrastructure.repository.AlvaraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class AlvaraService {
    private final AlvaraRepository repo;
    public AlvaraService(AlvaraRepository repo){ this.repo = repo; }
    public AlvaraEntity save(AlvaraEntity e){ return repo.save(e); }
}
