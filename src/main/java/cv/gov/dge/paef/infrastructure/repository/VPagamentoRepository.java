package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.VPaefPagamentoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface VPagamentoRepository extends Repository<VPaefPagamentoEntity, String> {

    @Query("""
    select p
    from VPaefPagamentoEntity p
    where p.idEntidade = :idEntidade
      and (:idAlvara is null or p.idAlvara = :idAlvara)
      and (:estado is null or p.dmEstadoPag = :estado)
      and (:nrProcesso is null or p.idProcesso = :nrProcesso)
    order by p.nrParcela asc
  """)
    List<VPaefPagamentoEntity> findPagamentos(
            @Param("idEntidade") String idEntidade,
            @Param("idAlvara") String idAlvara,
            @Param("estado") String estado,
            @Param("nrProcesso") String nrProcesso
    );


}
