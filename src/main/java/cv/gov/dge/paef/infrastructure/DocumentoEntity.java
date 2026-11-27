package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name="paef_t_documento", schema="paef")
@Getter @Setter
public class DocumentoEntity {

    @Id
    private String id;

    @Column(name="data_registo", nullable=false)
    private LocalDate dataRegisto;

    @Column(name="tipo_relacao")
    private String tipoRelacao;

    @Column(name="id_relacao")
    private String idRelacao;

    @Column(name="dm_tipo_documento")
    private String dmTipoDocumento;

    @Column(name="doc_clob")
    private String docClob;

    @Column(name="descricao")
    private String descricao;

    @Column(name="content_type", length=255)
    private String contentType;

    @Column(name="file")
    private byte[] file;
}
