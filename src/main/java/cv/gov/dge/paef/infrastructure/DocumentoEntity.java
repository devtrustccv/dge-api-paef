package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="paef_t_documento", schema="paef")
@Getter @Setter
public class DocumentoEntity {

    @Id
    private String id;

    @Column(name="tipo_relacao")
    private String tipoRelacao;

    @Column(name="id_relacao")
    private String idRelacao;

    @Column(name="dm_tipo_documento")
    private String dmTipoDocumento;

    @Column(name="doc_clob")
    private String docClob;
}
