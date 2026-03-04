package cv.gov.dge.paef.infrastructure;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tbl_clob", schema = "public")
@Getter
@Setter
public class ClobEntity {

    @Id
    @Column(name = "id")
    private Integer id; // ajusta se o teu id não for Long

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "c_lob_content", columnDefinition = "bytea")
    private byte[] file;
}