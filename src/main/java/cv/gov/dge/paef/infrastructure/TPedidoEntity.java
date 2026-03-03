package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "t_pedido", schema = "paef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TPedidoEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_entidade")
    private String idEntidade;

    @Column(name = "dm_estado_pedido")
    private String dmEstadoPedido;

    @Column(name = "id_tp_processo")
    private String idTpProcesso;

    @Column(name = "tp_pedido")
    private String tpPedido;

    @Column(name = "dm_origem_reg")
    private String dmOrigemReg;

    @Column(name = "id_user_reg")
    private BigDecimal idUserReg;

    @Column(name = "nome_entrega")
    private String nomeEntrega;

    @Column(name = "dt_registo")
    private LocalDate dtRegisto;

    @Column(name = "dt_fim")
    private LocalDate dtFim;

    @Column(name = "resultado")
    private String resultado;

    @Column(name = "id_processo")
    private BigDecimal idProcesso;

    @Column(name = "valor_pago")
    private BigDecimal valorPago;

    @Column(name = "dt_pagamento")
    private LocalDate dtPagamento;

    @Column(name = "id_etapa")
    private String idEtapa;

    @Column(name = "etapa_atual")
    private String etapaAtual;

    // getters/setters
}
