package cv.gov.dge.paef.interfaces.dto.notificacao;

import java.time.LocalDateTime;
import java.util.List;

public record NotificacaoMensagemDTO(String id, String idNotificacao, String email, String mensagem,
        LocalDateTime dataRegistro, LocalDateTime dataEnvio, List<NotificacaoAnexoDTO> anexos) {
    public NotificacaoMensagemDTO(String id, String idNotificacao, String email, String mensagem,
            LocalDateTime dataRegistro, LocalDateTime dataEnvio) {
        this(id, idNotificacao, email, mensagem, dataRegistro, dataEnvio, List.of());
    }
}