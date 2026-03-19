// src/main/java/cv/gov/dge/paef/infrastructure/repository/FormacaoRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoOutroDto;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoRow;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.RvccFormacaoRow;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormacaoRepository extends JpaRepository<FormacaoEntity, String> {
    List<FormacaoEntity> findByIdAlvara(String idAlvara);

    @Query(value = """
        select
          f.familia         as familia,
          f.dm_tipo_formacao as dmTipoFormacao,
          f.id_qualificacao  as idQualificacao,
          f.dm_nivel_romano as dmNivelRomano,
           f.dm_nivel_arabico   as dmNivelArabico,
          f.qualificacao     as qualificacao,
          f.dm_metodologia   as dmMetodologia
        from paef.v_paef_formacoes f
        where f.id_alvara = :idAlvara
        """, nativeQuery = true)
    List<FormacaoRow> findByAlvara(@Param("idAlvara") String idAlvara);

    @Query(value = """
    select
        f.id_alvara,
        a.nr_alvara,
        e.denominacao_social as entidade_nome,
        f.familia,
        f.qualificacao,
        coalesce(f.dm_nivel_romano, f.dm_nivel_arabico) as nivel,
        f.id_formacao
    from paef.v_paef_formacoes f
    join paef.paef_t_alvara a on a.id = f.id_alvara and a.dm_situacao='A'
    join paef.paef_t_entidade e on e.id = a.id_entidade
    where f.id_qualificacao = 'OUTRO'
      and (coalesce(:nr_alvara, '') = '' or a.nr_alvara = :nr_alvara)
      and (coalesce(:entidade_nome, '') = '' or e.denominacao_social ilike '%' || :entidade_nome || '%')
      and (coalesce(:familia, '') = '' or f.familia ilike '%' || :familia || '%')
      and (coalesce(:qualificacao, '') = '' or f.qualificacao ilike '%' || :qualificacao || '%')
      and (coalesce(:nivel, '') = '' or coalesce(f.dm_nivel_romano, f.dm_nivel_arabico) = :nivel)
    order by a.nr_alvara
""", nativeQuery = true)
    List<FormacaoOutroDto> findFormacoesOutro(
            @Param("nr_alvara") String nrAlvara,
            @Param("entidade_nome") String entidadeNome,
            @Param("familia") String familia,
            @Param("qualificacao") String qualificacao,
            @Param("nivel") String nivel
    );


    @Query(value = """
        select
            e.nif::text as nif,
            coalesce(e.denominacao_comercial, e.denominacao_social) as designacaoComercial,
            gi.nome as ilhaNome,
            gc.nome as concelhoNome,
            gc.id::text as concelhoId,
            coalesce(e.endereco, '') as endereco,
            coalesce(a.nr_alvara, '') as numAlvara,

            case
                when a.dm_estado_alvara = 'A' then 'Ativo'
                when a.dm_estado_alvara = 'CD' then 'Caducado'
                when a.dm_estado_alvara = 'CANC' then 'Cancelado'
                when a.dm_estado_alvara = 'S' then 'Suspenso'
                when a.dm_estado_alvara = 'ANUL' then 'Anulado'
                when a.dm_estado_alvara = 'RA' then 'Reativo'
                when a.dm_estado_alvara = 'REVOG' then 'Revogado'
                when a.dm_estado_alvara = 'ALARG' then 'Alargado'
                else coalesce(a.dm_estado_alvara, '')
            end as estadoAlvara,

            q.sigla_codigo as codigoCnq,
            q.self_id_cnq as selfidQp,
            coalesce(q.descricao, f.qualificacao_desc) as denominacao,
            fam.descricao as familia,
            fam.sigla_codigo as codigoFamilia,
            coalesce(f.dm_nivel_romano, q.dm_nivel_arabico) as nivelQnq,

            f.flag_rvcc as flagRvcc,
            f.sended_to_rvcc as sendedToRvcc,
            f.id as idFormacao

        from paef.paef_t_formacao f
        join paef.paef_t_alvara a
          on a.id = f.id_alvara
        join paef.paef_t_entidade e
          on e.id = a.id_entidade
        left join paef.geografia gz
          on gz.id::text = e.geog_local_id::text
        left join paef.geografia gc
          on gc.id::text = gz.concelho::text
        left join paef.geografia gi
          on gi.id::text = gz.ilha::text
        left join paef.paef_t_area_qualif q
          on q.id::text = f.id_qualificacao::text
        left join paef.paef_t_area_qualif fam
          on fam.id::text = q.id_pai::text

        where
            (f.flag_rvcc = true and f.sended_to_rvcc is null)
            or
            (f.sended_to_rvcc = 'true' and coalesce(f.flag_rvcc, false) = false)

        order by e.nif, a.nr_alvara
    """, nativeQuery = true)
    List<RvccFormacaoRow> findRvccFormacoes();

    @Modifying
    @Transactional
    @Query("""
    update FormacaoEntity f
    set f.sendedToRvcc = 'true'
    where f.id in :ids
      and f.flagRvcc = true
      and f.sendedToRvcc is null
""")
    int markAsSentToRvcc(@Param("ids") List<String> ids);
}
