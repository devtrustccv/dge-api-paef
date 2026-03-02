// src/main/java/cv/gov/dge/paef/infrastructure/repository/LogAlteracaoRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.LogAlteracaoEntity;
import cv.gov.dge.paef.interfaces.dto.logs.LogAlteracaoRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogAlteracaoRepository extends JpaRepository<LogAlteracaoEntity, String> {
    @Query(value = """
        select
          l.data_registo    as dataRegisto,
          COALESCE(l.user_registo_id::text,l.user_email) as userRegistoId,
          l.campo_alt       as campoAlt,
          l.valor_anterior  as valorAnterior,
          l.valor_atual     as valorAtual
        from paef.paef_t_log_alteracao l
        where l.tipo_relacao = 'ALVARA'
          and l.id_relacao = :idAlvara
          --and l.campo_alt in ('ESTADO', 'SITUACAO')
        order by l.data_registo asc
        """, nativeQuery = true)
    List<LogAlteracaoRow> findHistoricoAlvara(@Param("idAlvara") String idAlvara);

}
