package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(schema = "paef", name = "paef_t_area_qualif")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AreaQualifEntity {

    @Id
    @UuidGenerator           // << gera UUID v4
    @Column(nullable = false, length = 36)
    private String id; // usa codigoQualif

    @Column(name = "id_pai")
    private String idPai; // codigoFamilia

    @Column(name = "sigla_codigo", length = 50, nullable = false)
    private String siglaCodigo; // codigoQualif

    @Column(name = "dm_nivel_arabico", length = 50)
    private String dmNivelArabico; // nivel

    @Column(name = "descricao", length = 1000, nullable = false)
    private String descricao; // denominacaoQualif

    @Column(name = "estado", length = 1, nullable = false)
    private String estado; // default 'A'

    @Column(name = "self_id_cnq")
    private String selfIdCnq; // selfId

    @Column(name = "versao")
    private String versao; // versao
}
