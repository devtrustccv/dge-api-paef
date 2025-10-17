package cv.gov.dge.paef.domain.areaqualif.business;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.application.AreaQualif.service.AreaQualifService;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.mapper.AreaQualifMapper;
import cv.gov.dge.paef.infrastructure.repository.AreaQualifRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Transactional(readOnly = true)
    public AreaQualifEntity findFamilyByCode(String codigoFamilia) {
        return repo.findBySiglaCodigoIgnoreCaseAndIdPaiIsNull(codigoFamilia).orElse(null);
    }

    /** Cria família raiz (id_pai=NULL) com mínimo de campos. */
    public AreaQualifEntity createFamily(String codigoFamilia, String denominacaoFamilia) {
        String descricao = (denominacaoFamilia != null && !denominacaoFamilia.isBlank())
                ? denominacaoFamilia : codigoFamilia;

        AreaQualifEntity familia = AreaQualifEntity.builder()
                .siglaCodigo(codigoFamilia)
                .descricao(descricao)
                .estado("A")
                .build();
        // id_pai permanece null para marcar como família
        return repo.save(familia);
    }

    /**
     * Idempotente sob concorrência se usares o índice único (veja Flyway abaixo).
     * Em corrida, se outro request criar primeiro, apanhamos a DataIntegrityViolation e re-buscamos.
     */
    public AreaQualifEntity findOrCreateFamily(String codigoFamilia, String denominacaoFamilia) {
        AreaQualifEntity existente = findFamilyByCode(codigoFamilia);
        if (existente != null) return existente;
        try {
            return createFamily(codigoFamilia, denominacaoFamilia);
        } catch (DataIntegrityViolationException e) {
            // outro nó criou no meio do caminho → ler de novo
            AreaQualifEntity retry = findFamilyByCode(codigoFamilia);
            if (retry != null) return retry;
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public boolean existsQualification(String codigoQualif, String versao) {
        return repo.existsQualification(codigoQualif, versao);
    }

}
