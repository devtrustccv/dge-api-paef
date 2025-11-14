// src/main/java/cv/gov/dge/paef/infrastructure/repository/AlvaraRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlvaraRepository extends JpaRepository<AlvaraEntity, String> {
    List<AlvaraEntity> findByIdEntidade(String idEntidade);

    @Modifying
    @Query(value = "update paef.paef_t_alvara set sended_to_sgf = true where id = :id", nativeQuery = true)
    int markSgfSentById(@Param("id") String id);
}
