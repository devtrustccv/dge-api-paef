// src/main/java/cv/gov/dge/paef/infrastructure/repository/EntidadeRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.interfaces.dto.AcreditacaoProjection;
import cv.gov.dge.paef.interfaces.dto.EntidadeAcreditacaoRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EntidadeRepository extends JpaRepository<EntidadeEntity, BigDecimal> {

    Optional<EntidadeEntity> findByNif(BigDecimal nif);


    @Query(value = """
        select
               af.sigla_codigo                                   as codigoFamilia,     -- família (pai)
               coalesce(f.dm_nivel_romano, aq.dm_nivel_arabico)  as nivel,            -- <- fallback
               f.self_id_cnq                                     as self,
               f.versao                                          as versao,
               aq.sigla_codigo                                   as codigoQualificacao,
               coalesce(aq.descricao, f.qualificacao_desc)       as denominacaoQualificacao,
               f.dm_tipo_formacao                                as modalidade,
               f.dm_metodologia                                  as metodologia,
               a.nr_alvara                                       as nrAlvara,
               a.id                                              as idAlvara,
               a.dm_estado_alvara                                as statusAlvara,
               f.flag_catalogo                                   as flagCatalogo,
               a.dm_situacao										as situacao\s
             from paef.paef_t_entidade e
             left join paef.paef_t_alvara    a  on a.id_entidade = e.id
             left join paef.paef_t_formacao  f  on f.id_alvara   = a.id
             left join paef.paef_t_area_qualif aq on aq.id       = f.id_qualificacao          -- filho (qualificação/área)
             left join paef.paef_t_area_qualif af on af.id       = aq.id_pai                  -- pai (família)
             where e.nif = :nif;
        """, nativeQuery = true)
    List<AcreditacaoProjection> findAcreditacoesByNif(BigDecimal nif);

    @Query(value = """
            select
                  e.id                         as entId,
                  e.denominacao_social         as denominacao,
                  e.nif                        as nif,
                  e.dm_natureza                as natureza,
                  e.email                      as email,
                  e.url                        as url,
                  e.geog_local_id              as geogLocalId,
                  af.sigla_codigo                                   as codigoFamilia,
                  coalesce(f.dm_nivel_romano, aq.dm_nivel_arabico)  as nivel,
                  f.self_id_cnq                                     as self,
                  f.versao                                          as versao,
                  aq.sigla_codigo                                   as codigoQualificacao,
                  case when aq.sigla_codigo = 'OUTRO'
                       then f.qualificacao_desc
                       else coalesce(aq.descricao, f.qualificacao_desc)
                  end                                               as denominacaoQualificacao,
                  f.dm_tipo_formacao                                as modalidade,
                  f.dm_metodologia                                  as metodologia,
                  a.nr_alvara                                       as nrAlvara,
                  a.id                                              as idAlvara,
                  a.dm_estado_alvara                                as statusAlvara,
                  f.flag_catalogo                                   as flagCatalogo,
                  a.dm_situacao                                     as situacao
                from paef.paef_t_entidade e
                join paef.paef_t_alvara    a  on a.id_entidade   = e.id
                                              and (a.sended_to_sgf is distinct from true)
                left join paef.paef_t_formacao  f  on f.id_alvara     = a.id
                left join paef.paef_t_area_qualif aq on aq.id         = f.id_qualificacao
                left join paef.paef_t_area_qualif af on af.id         = f.id_area_qualif;
        """, nativeQuery = true)
    List<EntidadeAcreditacaoRow> findEntidadesComAlvaraNaoEnviadoSgf();
}
