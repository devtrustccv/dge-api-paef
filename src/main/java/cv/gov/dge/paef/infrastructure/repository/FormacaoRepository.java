// src/main/java/cv/gov/dge/paef/infrastructure/repository/FormacaoRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoOutroDto;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoRow;
import org.springframework.data.jpa.repository.JpaRepository;
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
        coalesce(f.dm_nivel_romano,f.dm_nivel_arabico) nivel
    from paef.v_paef_formacoes f
    join paef.paef_t_alvara a on a.id = f.id_alvara
    join paef.paef_t_entidade e on e.id = a.id_entidade
    where f.id_qualificacao = 'OUTRO'
    order by a.nr_alvara
""", nativeQuery = true)
    List<FormacaoOutroDto> findFormacoesOutro();
}
