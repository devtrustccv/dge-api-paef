package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.ContaAcessoEntity;
import cv.gov.dge.paef.interfaces.dto.ContaAcesso.ContaAcessoRow;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeContaRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ContaAcessoRepository extends JpaRepository<ContaAcessoEntity, String> {

    @Query("""
    select count(c) > 0
    from ContaAcessoEntity c
    join EntidadeEntity e on e.id = c.idEntidade
    where e.nif = :nif
      and c.emailUser = :emailUser
      and c.flagMaster = 'SIM'
      and c.dmEstadoConta = 'A'
""")
    boolean existsMasterByNifAndEmail(
            @Param("nif") BigDecimal nif,
            @Param("emailUser") String emailUser
    );

    @Query(value = """
    select
        pte.nif::text              as nif,
        pte.denominacao_social     as denominacao,
        ptca.flag_master           as flagMaster
    from paef.paef_t_conta_acesso ptca
    join paef.paef_t_entidade pte on pte.id = ptca.id_entidade
    where lower(ptca.email_user) = lower(:emailUser)
      and ptca.dm_estado_conta = 'A'
    order by pte.denominacao_social
    """, nativeQuery = true)
    List<EntidadeContaRow> findEntidadesByUser(@Param("emailUser") String emailUser);
    @Query(value = """
        select
            ca.id                         as id,
            ca.email_user                 as emailUser,
            ca.flag_master                as flagMaster,
            ca.data_registo               as dataRegisto,
            ca.dm_estado_conta            as dmEstadoConta
        from paef.paef_t_conta_acesso ca
        join paef.paef_t_entidade e on e.id = ca.id_entidade
        where e.nif = :nif
        order by ca.email_user asc, ca.dm_estado_conta asc
        """, nativeQuery = true)
    List<ContaAcessoRow> findAllByEntidadeNif(@Param("nif") BigDecimal nif);

    List<ContaAcessoEntity> findByIdEntidadeAndIdNot(String idEntidade, String id);
}
