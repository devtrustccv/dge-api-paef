// src/main/java/cv/gov/dge/paef/domain/formacao/FormacaoService.java
package cv.gov.dge.paef.application.formacao.service;

import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.infrastructure.repository.FormacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class FormacaoService {
    private final FormacaoRepository repo;
    public FormacaoService(FormacaoRepository repo){ this.repo = repo; }
    public FormacaoEntity save(FormacaoEntity e){ return repo.save(e); }
}
