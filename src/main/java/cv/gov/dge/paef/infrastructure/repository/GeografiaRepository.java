// src/main/java/cv/gov/dge/paef/infrastructure/repository/GeografiaRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.GeografiaEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GeografiaRepository extends JpaRepository<GeografiaEntity, String> {

    // Ilhas: pais=238, ilha is null, geogr_id not null
    @Query(value = """
        select g.*
          from paef.geografia g
         where g.pais = '238'
           and g.ilha is null
           and g.geogr_id is not null
         order by g.nome
        """, nativeQuery = true)
    List<GeografiaEntity> findIlhas();

    // Concelhos por ilha: concelho is null, geogr_id not null, ilha not null, nome != SANTA LUZIA, ilha = :ilha
    @Query(value = """
        select g.*
          from paef.geografia g
         where g.pais = '238'
           and g.concelho is null
           and g.geogr_id is not null
           and g.ilha is not null
           and g.nome not like 'SANTA LUZIA'
           and g.ilha = :ilha
         order by g.nome
        """, nativeQuery = true)
    List<GeografiaEntity> findConcelhosByIlha(@Param("ilha") String ilha);

    // Freguesias por concelho: freguesia is null, ... concelho = :concelho
    @Query(value = """
        select g.*
          from paef.geografia g
         where g.pais = '238'
           and g.freguesia is null
           and g.geogr_id is not null
           and g.ilha is not null
           and g.nome not like 'SANTA LUZIA'
           and g.concelho = :concelho
         order by g.nome
        """, nativeQuery = true)
    List<GeografiaEntity> findFreguesiasByConcelho(@Param("concelho") String concelho);

    // Zonas por freguesia: zona is not null, ... freguesia = :freguesia
    @Query(value = """
        select g.*
          from paef.geografia g
         where g.pais = '238'
           and g.zona is not null
           and g.geogr_id is not null
           and g.ilha is not null
           and g.nome not like 'SANTA LUZIA'
           and g.freguesia = :freguesia
         order by g.nome
        """, nativeQuery = true)
    List<GeografiaEntity> findZonasByFreguesia(@Param("freguesia") String freguesia);
}
