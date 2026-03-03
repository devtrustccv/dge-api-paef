package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.PagamentoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PagamentoRepository extends Repository<PagamentoEntity, String> {

    @Query(value = """
    select coalesce(sum(valor),0)
    from paef.paef_t_pagamento
    where dm_estado_pag = :estado
      and id_entidade = :idEntidade
  """, nativeQuery = true)
    BigDecimal sumByEstado(@Param("estado") String estado, @Param("idEntidade") String idEntidade);

    @Query(value = """
    select coalesce(sum(valor),0)
    from paef.paef_t_pagamento
    where dm_estado_pag = 'PENDENTE'
      and data_prev_pag < now()
      and id_entidade = :idEntidade
  """, nativeQuery = true)
    BigDecimal dividaAtraso(@Param("idEntidade") String idEntidade);
}
