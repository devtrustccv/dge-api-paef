package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<DocumentoEntity, String> {
    List<DocumentoEntity> findByTipoRelacaoAndIdRelacao(String tipoRelacao, String idRelacao);
}
