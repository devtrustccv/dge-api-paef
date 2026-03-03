package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.View;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Immutable
@Table(name="v_paef_pagamentos", schema="paef")
@Getter
@Setter
public class VPaefPagamentoEntity {

    @Id
    @Column(name="id_pagamento")
    private String idPagamento;

    @Column(name="id_entidade")
    private String idEntidade;

    @Column(name="id_alvara")
    private String idAlvara;

    @Column(name="nr_alvara")
    private String nrAlvara;

    @Column(name="data_registo")
    private LocalDate dataRegisto;

    @Column(name="data_pag")
    private LocalDate dataPag;

    @Column(name="data_prev_pag")
    private LocalDate dataPrevPag;

    @Column(name="dm_estado_pag")
    private String dmEstadoPag;

    @Column(name="duc")
    private String duc;

    @Column(name="doc_clob")
    private String docClob;

    @Column(name="estado_pagamento_descricao")
    private String estadoPagamento; // ex: ATRASADO

    @Column(name="entidade")
    private String entidade;

    @Column(name="referencia")
    private BigDecimal referencia;

    @Column(name="valor")
    private BigDecimal valor;

    @Column(name="id_processo")
    private String idProcesso;

    @Column(name="form_pagamento")
    private String formPagamento;

    @Column(name="nr_parcela")
    private String nrParcela;

    // getters
}
