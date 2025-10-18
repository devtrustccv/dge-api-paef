// src/main/java/cv/gov/dge/paef/infrastructure/repository/AlvaraRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlvaraRepository extends JpaRepository<AlvaraEntity, String> {
    List<AlvaraEntity> findByIdEntidade(String idEntidade);
}
