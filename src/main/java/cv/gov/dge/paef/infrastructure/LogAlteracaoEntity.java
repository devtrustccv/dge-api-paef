// src/main/java/cv/gov/dge/paef/infrastructure/entity/LogAlteracaoEntity.java
package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(schema = "paef", name = "paef_t_log_alteracao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LogAlteracaoEntity {

    @Id
    @Column(name = "id")
    private String id; // se teu id for uuid string (ou ajusta)

    @Column(name = "valor_anterior")
    private String valorAnterior;

    @Column(name = "valor_atual")
    private String valorAtual;

    @Column(name = "tipo_relacao")
    private String tipoRelacao;

    @Column(name = "id_relacao")
    private String idRelacao;

    @Column(name = "campo_alt")
    private String campoAlt;

    @Column(name = "data_registo")
    private LocalDate dataRegisto;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "referencia")
    private String referencia;

    @Column(name = "motivo")
    private String motivo;
}
