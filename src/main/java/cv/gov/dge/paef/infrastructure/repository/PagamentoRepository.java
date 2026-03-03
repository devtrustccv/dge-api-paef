package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<PagamentoEntity, String> {

    @Query(value = """
    select coalesce(sum(valor),0)
    from PagamentoEntity
    where dmEstadoPag = :estado
      and idEntidade = :idEntidade
  """)
    BigDecimal sumByEstado(@Param("estado") String estado, @Param("idEntidade") String idEntidade);

    @Query(value = """
    select coalesce(sum(valor),0)
    from PagamentoEntity
    where dmEstadoPag = 'PENDENTE'
      and dataPrevPag < CURRENT_DATE
      and idEntidade = :idEntidade
  """)
    BigDecimal dividaAtraso(@Param("idEntidade") String idEntidade);

    Optional<PagamentoEntity> findByIdAndIdEntidade(String id, String idEntidade);

    Optional<PagamentoEntity> findByDuc(String duc);

}
