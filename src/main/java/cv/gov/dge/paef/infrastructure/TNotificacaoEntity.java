package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_notificacao", schema = "paef")
@Getter
@Setter
public class TNotificacaoEntity {

    @Id
    private String id;

    @Column(name = "tp_relacao")
    private String tpRelacao;

    @Column(name = "tp_relacao_id")
    private String tpRelacaoId; // aqui é id_entidade

    @Column(name="id_alvara")
    private String idAlvara;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_alvara", referencedColumnName="id", insertable=false, updatable=false)
    private AlvaraEntity alvara;

    @Column(name = "assunto")
    private String assunto;

    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "tipo")
    private String tipo; // meio envio

    @Column(name = "id_processo")
    private String idProcesso;

    @Column(name = "id_pagina_etapa")
    private String idPaginaEtapa; // âmbito

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    // getters/setters
}
