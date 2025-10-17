package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaQualifRepository extends JpaRepository<AreaQualifEntity, String> {
    boolean existsBySiglaCodigo(String siglaCodigo);
}
