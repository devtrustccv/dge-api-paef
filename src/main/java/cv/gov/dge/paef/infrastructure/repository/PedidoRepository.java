package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.TPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

public interface PedidoRepository extends JpaRepository<TPedidoEntity, String>, JpaSpecificationExecutor<TPedidoEntity> {
    Optional<TPedidoEntity> findByIdProcesso(BigDecimal idProcesso);

    List<TPedidoEntity> findByIdEntidadeAndEtapaAtualInOrderByDtRegistoDescIdDesc(
            String idEntidade, List<String> etapas);
}
