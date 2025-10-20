// src/main/java/cv/gov/dge/paef/infrastructure/entity/EntidadeEntity.java
package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(schema = "paef", name = "paef_t_entidade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntidadeEntity {
    @Id
    @UuidGenerator
    @Column(length = 128, nullable = false)
    private String id;

    @Column(name="denominacao_social", nullable=false, length=500)
    private String denominacaoSocial;

    @Column(name="denominacao_comercial", length=500)
    private String denominacaoComercial;

    @Column(nullable=false)
    private BigDecimal nif;

    @Column(name="dm_natureza", length=50)
    private String dmNatureza;

    @Column(name="dt_constituicao")
    private LocalDate dtConstituicao;

    @Column(name="nr_matricula", length=128)
    private String nrMatricula;

    @Column(length=200)
    private String endereco;

    @Column(name="caixa_postal", length=50)
    private String caixaPostal;

    @Column(length=50)
    private String telefone;

    @Column(length=50)
    private String telemovel;

    @Column(length=200)
    private String email;

    @Column(length=50)
    private String url;

    @Column(name="representante_legal", length=50)
    private String representanteLegal;

    @Column(name="dm_origem_reg", length=10, nullable=false)
    private String dmOrigemReg;

    @Column(name="data_registo", nullable=false)
    private LocalDate dataRegisto;

    @Column(name="user_registo_id", nullable=false)
    private BigDecimal userRegistoId;

    @Column
    private String abreviatura;

    @Column(name="geog_local_id")
    private String geogLocalId;

    @Column(name="denominacao_social_norm", length=500)
    private String denominacaoSocialNorm;

    @Column(name="is_on_portal")
    private Boolean isOnPortal;

    @Column(name = "sended_to_pffp")
    private Boolean sendedToPffp;
}
