package cv.gov.dge.paef.infrastructure.repository;


import cv.gov.dge.paef.infrastructure.ClobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClobRepository extends JpaRepository<ClobEntity, Long> {
    Optional<ClobEntity> findByUuid(String uuid);
}
