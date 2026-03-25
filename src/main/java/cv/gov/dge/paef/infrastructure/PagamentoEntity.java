package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "paef_t_pagamento", schema = "paef")
@Getter
@Setter
public class PagamentoEntity {

    @Id
    @Column(name = "id", nullable = false, length = 128)
    private String id;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString().replace("-", "");
        }
    }

    @Column(name = "id_alvara")
    private String idAlvara;

    @Column(name = "entidade")
    private String entidade;

    @Column(name = "referencia")
    private BigDecimal referencia;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "duc", length = 100)
    private String duc;

    @Column(name = "dm_estado_pag", nullable = false, length = 50)
    private String dmEstadoPag;

    @Column(name = "data_registo", nullable = false)
    private LocalDate dataRegisto;

    @Column(name = "data_prev_pag")
    private LocalDate dataPrevPag;

    @Column(name = "data_pag")
    private LocalDate dataPag;

    @Column(name = "id_entidade", nullable = false)
    private String idEntidade;

    @Column(name = "user_registo_id")
    private BigDecimal userRegistoId;

    @Column(name = "user_delete_id")
    private BigDecimal userDeleteId;

    @Column(name = "doc_clob")
    private String docClob;

    @Column(name = "id_processo")
    private String idProcesso;

    @Column(name = "id_task")
    private String idTask;

    @Column(name = "nr_parcela")
    private String nrParcela;

    @Column(name = "id_pedido")
    private String idPedido;

    @Column(name = "custa_id")
    private String custaId;

    @Column(name = "nu_cheque")
    private String nuCheque;

    @Column(name = "form_pagamento")
    private String formPagamento;

    @Column(name = "dt_pagamento")
    private LocalDate dtPagamento;

    @Column(name = "dt_update")
    private LocalDate dtUpdate;

    @Column(name = "user_pag_id", precision = 19, scale = 2)
    private BigDecimal userPagId;

    @Column(name = "user_update_id", precision = 19, scale = 2)
    private BigDecimal userUpdateId;

    @Column(name = "dmtipopagamento")
    private String dmtipopagamento;

    @Column(name = "dm_tipo_pagamento")
    private String dmTipoPagamento;

    @Column(name = "banco", length = 255)
    private String banco;

    @Column(name = "link_duc", length = 500)
    private String linkDuc;

    @Column(name = "data_checked")
    private LocalDate dataChecked;
}
