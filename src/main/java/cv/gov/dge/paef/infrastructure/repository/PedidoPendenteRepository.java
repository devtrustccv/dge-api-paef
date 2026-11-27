package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.TPedidoPendenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoPendenteRepository extends JpaRepository<TPedidoPendenteEntity, Integer> {
    List<TPedidoPendenteEntity> findByIdEntidadeAndDmEstadoOrderByDataPedidoDescIdDesc(
            String idEntidade, String dmEstado);
}
