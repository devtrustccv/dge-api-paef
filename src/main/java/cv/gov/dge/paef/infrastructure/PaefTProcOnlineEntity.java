package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// infrastructure/entity/PaefTProcOnlineEntity.java
@Entity
@Table(schema="paef", name="paef_t_proc_online")
@Getter
@Setter
public class PaefTProcOnlineEntity {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="id_tp_processo")
    private String idTpProcesso;

    @Column(name="ref_pagina")
    private String refPagina;

    @Column(name="dm_estado_proc_online")
    private String dmEstadoProcOnline;

    @Column(name="doc_clob")
    private String docClob;

    @Column(name="user_registo_id")
    private BigDecimal userRegistoId;

    @Column(name="user_update_id")
    private BigDecimal userUpdateId;

    @Column(name="dt_registo")
    private LocalDate dtRegisto;

    @Column(name="data_update")
    private LocalDate dataUpdate;
}
