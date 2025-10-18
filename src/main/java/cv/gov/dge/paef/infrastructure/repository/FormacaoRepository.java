// src/main/java/cv/gov/dge/paef/infrastructure/repository/FormacaoRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormacaoRepository extends JpaRepository<FormacaoEntity, String> {
    List<FormacaoEntity> findByIdAlvara(String idAlvara);
}
