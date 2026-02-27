package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.PaefTEquipamentoEntity;
import cv.gov.dge.paef.interfaces.dto.EquipamentoRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<PaefTEquipamentoEntity, String> {

    @Query(value = """
        select e.descricao as descricao
        from paef.paef_t_equipamento e
        where e.id_alvara = :idAlvara
        """, nativeQuery = true)
    List<EquipamentoRow> findByAlvara(@Param("idAlvara") String idAlvara);
}
