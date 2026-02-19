// src/main/java/cv/gov/dge/paef/infrastructure/repository/AlvaraRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.interfaces.dto.Alvara.AlvaraOptionRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AlvaraRepository extends JpaRepository<AlvaraEntity, String> {
    List<AlvaraEntity> findByIdEntidade(String idEntidade);

    @Modifying
    @Query(value = "update paef.paef_t_alvara set sended_to_sgf = true where id = :id", nativeQuery = true)
    int markSgfSentById(@Param("id") String id);

    @Query(value = """
        select
          pta.id as key,
          (pta.nr_alvara || ' - ' || ptta.descricao) as value
        from paef.paef_t_alvara pta
        join paef.paef_t_tipo_alvara ptta on ptta.id = pta.id_tipo_alvara
        join paef.paef_t_entidade e on e.id = pta.id_entidade
        where e.nif = :nif
        order by pta.nr_alvara
        """, nativeQuery = true)
    List<AlvaraOptionRow> findAlvarasOptionsByNif(@Param("nif") BigDecimal nif);
}
