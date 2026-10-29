package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "paef_t_notificacao_mensagem", schema = "paef")
@Getter
@Setter
public class NotificacaoMensagemEntity {
    @Id
    @Column(length = 128)
    private String id;

    @Column(name = "id_notificacao", nullable = false, length = 128)
    private String idNotificacao;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(nullable = false, columnDefinition = "text")
    private String mensagem;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;
}
