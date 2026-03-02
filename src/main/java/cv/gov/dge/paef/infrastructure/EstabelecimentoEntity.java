package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="paef_t_estabelecimento", schema="paef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstabelecimentoEntity {

    @Id
    private String id;

    @Column(name="geog_local_id")
    private String geogLocalId;

    @Column(name="telefone")
    private BigDecimal telefone;

    @Column(name="telemovel")
    private BigDecimal telemovel;

    @Column(name="endereco")
    private String endereco;

    @Column(name="caixa_postal")
    private String caixaPostal;

    // Responsável
    @Column(name="nome_resp")
    private String nomeResp;

    @Column(name="dm_tp_doc")
    private String dmTpDoc;

    @Column(name="nr_doc_resp")
    private String nrDocResp;

    @Column(name="telefone_resp")
    private String telefoneResp;

    @Column(name="telemovel_resp")
    private String telemovelResp;

    @Column(name="email_resp")
    private String emailResp;

    @Column(name="endereco_resp")
    private String enderecoResp;

    @Column(name="dm_nivel_academico")
    private String dmNivelAcademico;

    @Column(name="designacao")
    private String designacao;

    // getters/setters
}
