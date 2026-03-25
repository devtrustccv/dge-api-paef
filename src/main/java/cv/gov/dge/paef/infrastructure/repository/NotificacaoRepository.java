package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.TNotificacaoEntity;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacaoRepository
        extends JpaRepository<TNotificacaoEntity, String>, JpaSpecificationExecutor<TNotificacaoEntity> {
    @Query("""
  select
    n.id as id,
    n.assunto as assunto,
    n.dataRegistro as dataRegistro,
    n.tipo as tipo,
    n.idProcesso as idProcesso,
    n.idPaginaEtapa as idPaginaEtapa,
    coalesce(a.nrAlvara, '') as nrAlvara
  from TNotificacaoEntity n
  left join n.alvara a
  where n.tpRelacao = 'ENTIDADE'
    and n.tpRelacaoId = :idEntidade
    and (:idAlvara is null or n.idAlvara = :idAlvara)
  order by n.dataRegistro desc
""")
    List<NotificacaoRow> findNotificacoes(
            @Param("idEntidade") String idEntidade,
            @Param("idAlvara") String idAlvara
    );

    @Query("""
  select
    n.id as id,
    n.assunto as assunto,
    n.dataRegistro as dataRegistro,
    n.tipo as tipo,
    n.idProcesso as idProcesso,
    n.idPaginaEtapa as idPaginaEtapa,
    coalesce(a.nrAlvara, '') as nrAlvara
  from TNotificacaoEntity n
  left join n.alvara a
  where n.tpRelacao = 'ENTIDADE'
    and n.tpRelacaoId = :idEntidade
    and (:idAlvara is null or n.idAlvara = :idAlvara)
    and n.dataEnvio between :dataDe and :dataAte
  order by n.dataRegistro desc
""")
    List<NotificacaoRow> findNotificacoesByPeriodo(
            @Param("idEntidade") String idEntidade,
            @Param("idAlvara") String idAlvara,
            @Param("dataDe") LocalDateTime dataDe,     // ou LocalDate (depende do teu campo)
            @Param("dataAte") LocalDateTime dataAte
    );
    }