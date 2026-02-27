package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "paef_t_equipamento", schema = "paef")
public class PaefTEquipamentoEntity {
    @Id
    private String id;

    @Column(name="id_alvara")
    private String idAlvara;

    @Column(name="descricao")
    private String descricao;
}
