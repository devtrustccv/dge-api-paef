package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(schema="paef", name="paef_t_conta_acesso")
@Getter
@Setter
public class ContaAcessoEntity {

    @Id
    @GeneratedValue(generator = "uuid-no-dash")
    @GenericGenerator(
            name = "uuid-no-dash",
            strategy = "cv.gov.dge.paef.helpers.CustomUuidGenerator"
    )
    @Column(length = 32, nullable = false)
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
