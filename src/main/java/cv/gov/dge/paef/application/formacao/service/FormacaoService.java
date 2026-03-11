// src/main/java/cv/gov/dge/paef/domain/formacao/FormacaoService.java
package cv.gov.dge.paef.application.formacao.service;

import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.infrastructure.repository.FormacaoRepository;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoOutroDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class FormacaoService {
    private final FormacaoRepository repo;
    public FormacaoService(FormacaoRepository repo){ this.repo = repo; }
    public FormacaoEntity save(FormacaoEntity e){ return repo.save(e); }
    public List<FormacaoOutroDto> listarFormacoesOutro() {

        return repo.findFormacoesOutro()
                .stream()
                .map(f -> FormacaoOutroDto.builder()
                        .idAlvara(f.getIdAlvara())
                        .nrAlvara(f.getNrAlvara())
                        .entidadeNome(f.getEntidadeNome())
                        .familia(f.getFamilia())
                        .qualificacao(f.getQualificacao())
                        .nivel(f.getNivel())
                        .build())
                .toList();
    }
}
