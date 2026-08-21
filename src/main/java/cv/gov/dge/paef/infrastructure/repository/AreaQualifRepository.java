package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.interfaces.dto.OptionRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
    Optional<AreaQualifEntity> findBySiglaCodigoAndVersao(String siglaCodigo, String versao);

    @Query(value = """
        select
          q.id as key,
          q.descricao as value
        from paef.paef_t_area_qualif q
        where q.id_pai = :idFamilia
          and q.estado = 'A'
        order by q.descricao
        """, nativeQuery = true)
    List<OptionRow> findQualificacoesByFamilia(@Param("idFamilia") String idFamilia);
}

