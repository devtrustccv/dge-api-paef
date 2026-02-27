package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "paef_t_rh", schema = "paef")
public class PaefTRhEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id_alvara")
    private String idAlvara;

    @Column(name = "dm_funcao")
    private String dmFuncao;

    @Column(name = "quantidade_total")
    private BigDecimal quantidadeTotal;

    // getters/setters
}
