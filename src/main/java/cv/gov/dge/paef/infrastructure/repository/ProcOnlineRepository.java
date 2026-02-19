package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.PaefTProcOnlineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// infrastructure/repository/ProcOnlineRepository.java
public interface ProcOnlineRepository extends JpaRepository<PaefTProcOnlineEntity, String> {
    List<PaefTProcOnlineEntity> findByIdTpProcesso(String idTpProcesso);
}

