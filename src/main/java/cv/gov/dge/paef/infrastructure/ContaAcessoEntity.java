package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(schema="paef", name="paef_t_conta_acesso")
@Getter
@Setter
public class ContaAcessoEntity {

    @Id
    private String id;

    @Column(name="id_entidade")
    private String idEntidade;

    @Column(name="email_user")
    private String emailUser;

    @Column(name="flag_master")
    private String flagMaster;

    @Column(name="dm_estado_conta")
    private String dmEstadoConta;

    @Column(name="data_registo")
    private LocalDate dataRegisto;

    @Column(name="user_registo")
    private String userRegisto;

    @Column(name="dm_origem_reg")
    private String dmOrigemReg;
}
