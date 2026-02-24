package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.DomainViewEntity;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<DomainViewEntity, String> {

    @org.springframework.data.jpa.repository.Query(value = """
        select valor as key, description as value
        from paef.v_paef_dominio_igrp
        where dominio = :dominio
        order by description asc
    """, nativeQuery = true)
    java.util.List<OptionDTO> findByDominio(@org.springframework.data.repository.query.Param("dominio") String dominio);

    @org.springframework.data.jpa.repository.Query(value = """
        select description
        from paef.v_paef_dominio_igrp
        where lower(dominio) = lower(:dominio)
          and lower(valor) = lower(:valor)
        limit 1
    """, nativeQuery = true)
    String findDescByDominioAndValor(
            @org.springframework.data.repository.query.Param("dominio") String dominio,
            @org.springframework.data.repository.query.Param("valor") String valor
    );
}
