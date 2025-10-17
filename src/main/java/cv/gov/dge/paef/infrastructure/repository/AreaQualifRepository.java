package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaQualifRepository extends JpaRepository<AreaQualifEntity, String> {
    boolean existsBySiglaCodigo(String siglaCodigo);

    // Família: id_pai IS NULL e sigla_codigo = codigoFamilia
    Optional<AreaQualifEntity> findBySiglaCodigoAndIdPaiIsNull(String siglaCodigo);

    // (opcional) Qualificação por sigla + id_pai = família (útil para validações futuras)
    Optional<AreaQualifEntity> findBySiglaCodigoAndIdPai(String siglaCodigo, String idPai);

    Optional<AreaQualifEntity> findBySiglaCodigoIgnoreCaseAndIdPaiIsNull(String siglaCodigo);

    // qualificação (id_pai IS NOT NULL) por código + versão
    Optional<AreaQualifEntity> findFirstBySiglaCodigoIgnoreCaseAndVersaoIgnoreCaseAndIdPaiIsNotNull(
            String siglaCodigo, String versao
    );

    // atalho
    default boolean existsQualification(String siglaCodigo, String versao) {
        return findFirstBySiglaCodigoIgnoreCaseAndVersaoIgnoreCaseAndIdPaiIsNotNull(siglaCodigo, versao).isPresent();
    }
}

