// src/main/java/cv/gov/dge/paef/infrastructure/entity/FormacaoEntity.java
package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(schema = "paef", name = "paef_t_formacao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FormacaoEntity {
    @Id
    @UuidGenerator
    @Column(length = 128, nullable = false)
    private String id;

    @Column(name="id_alvara", nullable=false, length=128)
    private String idAlvara;

    @Column(name="dm_tipo_formacao", nullable=false, length=100)
    private String dmTipoFormacao;    // -> modalidade

    @Column(name="id_area_qualif", nullable=false)
    private String idAreaQualif;      // join com paef_t_area_qualif.id

    @Column(name="id_qualificacao", nullable=false)
    private String idQualificacao;    // -> codigo_qualificacao

    @Column(name="area_desc", length=1000)
    private String areaDesc;

    @Column(name="qualificacao_desc", length=1000)
    private String qualificacaoDesc;  // -> denominacao_qualificacao

    @Column(name="dm_nivel_romano", length=50)
    private String dmNivelRomano;     // -> nivel

    @Column(name="flag_catalogo", nullable=false, length=5)
    private String flagCatalogo;

    @Column(name="dt_registo")
    private LocalDate dtRegisto;

    @Column(name="dt_fim_prev")
    private LocalDate dtFimPrev;

    @Column(name="dt_ini_prev")
    private LocalDate dtIniPrev;

    @Column(name="dm_metodologia", length=255)
    private String dmMetodologia;     // -> metodologia

    @Column(name="self_id_cnq")
    private String selfIdCnq;         // -> self

    @Column(name="dm_estado")
    private String dmEstado;

    @Column
    private String versao;            // -> versao
}
