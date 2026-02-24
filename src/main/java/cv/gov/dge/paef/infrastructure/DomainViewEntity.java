package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="paef", name="v_paef_dominio_igrp")
@Getter
@Setter
public class DomainViewEntity {
    @Id
    @Column(name="id") // se não existir id real na view, tens de criar um id sintético
    private Integer id;

    @Column(name="dominio")
    private String dominio;

    @Column(name="valor")
    private String valor;

    @Column(name="description")
    private String description;
}
