package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.TPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PedidoRepository extends JpaRepository<TPedidoEntity, String>, JpaSpecificationExecutor<TPedidoEntity> {
}
