// src/main/java/cv/gov/dge/paef/infrastructure/entity/GeografiaEntity.java
package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "paef", name = "geografia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GeografiaEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "pais")
    private String pais;

    @Column(name = "ilha")
    private String ilha;

    @Column(name = "concelho")
    private String concelho;

    @Column(name = "freguesia")
    private String freguesia;

    @Column(name = "zona")
    private String zona;

    @Column(name = "geogr_id")
    private String geogrId;
}
