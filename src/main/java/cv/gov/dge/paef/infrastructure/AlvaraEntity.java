// src/main/java/cv/gov/dge/paef/infrastructure/entity/AlvaraEntity.java
package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(schema = "paef", name = "paef_t_alvara")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlvaraEntity {
    @Id
    @UuidGenerator
    @Column(length = 128, nullable = false)
    private String id;

    @Column(name="id_entidade", nullable=false, length=128)
    private String idEntidade;

    @Column(name="id_pedido", length=128)
    private String idPedido;

    @Column(name="id_tipo_alvara", nullable=false, length=128)
    private String idTipoAlvara;

    @Column(name="id_processo", length=500)
    private String idProcesso;

    @Column(name="dm_estado_alvara", nullable=false, length=50)
    private String dmEstadoAlvara;

    @Column(name="data_pedido")
    private LocalDate dataPedido;

    @Column(name="data_emissao", nullable=false)
    private LocalDate dataEmissao;

    @Column(name="data_validade", nullable=false)
    private LocalDate dataValidade;

    @Column(name="id_estabelecimento", length=128)
    private String idEstabelecimento;

    @Column(name="nr_processo")
    private String nrProcesso;

    @Column(name="dm_tipo_validade")
    private String dmTipoValidade;

    @Column(name="data_despacho")
    private LocalDate dataDespacho;

    @Column(length=1000)
    private String observacao;

    @Column(name="dm_situacao")
    private String dmSituacao;

    @Column
    private String parceria;

    @Column(name="nr_alvara")
    private String nrAlvara;

    @Column(name="dm_origem_reg", length=10)
    private String dmOrigemReg;

    @Column(name="dt_env_incv")
    private LocalDate dtEnvIncv;

    @Column(name="referencia_bo")
    private String referenciaBo;

    @Column(name="dt_pub_bo")
    private LocalDate dtPubBo;

    @Column(name="obs_bo")
    private String obsBo;

    @Column(name="link_report_alvara", length=500)
    private String linkReportAlvara;

    @Column(name="flag_publicacao")
    private String flagPublicacao;

    @Column(name="contra_prova", length=500)
    private String contraProva;
}
