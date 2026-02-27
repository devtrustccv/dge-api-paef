package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.PaefTRhEntity;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RhRepository extends JpaRepository<PaefTRhEntity, String> {

    @Query(value = """
        select r.dm_funcao as key,
               r.quantidade_total::text as value
        from paef.paef_t_rh r
        where r.id_alvara = :idAlvara
        """, nativeQuery = true)
    List<OptionDTO> findByAlvara(@Param("idAlvara") String idAlvara);
}