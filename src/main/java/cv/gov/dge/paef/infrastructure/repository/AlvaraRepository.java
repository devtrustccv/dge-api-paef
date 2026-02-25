// src/main/java/cv/gov/dge/paef/infrastructure/repository/AlvaraRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.interfaces.dto.Alvara.AlvaraOptionRow;
import cv.gov.dge.paef.interfaces.dto.Alvara.VPaefAlvaraRow;
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

    @Query(value = """
    select
      v.id_entidade           as idEntidade,
      v.id_alvara             as idAlvara,
      v.id_processo           as idProcesso,
      v.nr_alvara             as nrAlvara,
      v.designacao            as designacao,
      v.descricao_tipo_alvara as descricaoTipoAlvara,
      gz.nome                 as localizacao,
      v.data_emissao          as dataEmissao,
      v.data_validade         as dataValidade,
      v.dm_estado_alvara      as dmEstadoAlvara,
      v.estado_alvara_descricao as estadoAlvaraDescricao,
      v.dm_situacao           as dmSituacao,
      v.flag_publicacao       as flagPublicacao,
      v.dm_origem_reg         as dmOrigemReg,
      case when d.id is not null then true else false end as docAlvaraExists
    from paef.v_paef_alvara v
    left join paef.geografia gz
      on gz.id::text = v.local_estabelecimento::text
    left join paef.paef_t_documento d
      on d.id_relacao = v.id_alvara
     and d.tipo_relacao = 'ALVARA'
     and d.dm_tipo_documento = 'Alvará'
    where v.nif = :nif
      and v.dm_situacao = :situacao
    order by v.dm_situacao asc, v.nr_alvara asc
    limit :limit
    """, nativeQuery = true)
    List<VPaefAlvaraRow> findValidosByNif(
            @Param("nif") BigDecimal nif,
            @Param("situacao") String situacao,
            @Param("limit") int limit
    );
}
