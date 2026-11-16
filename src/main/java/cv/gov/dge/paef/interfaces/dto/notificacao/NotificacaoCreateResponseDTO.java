package cv.gov.dge.paef.interfaces.dto.notificacao;

import java.time.LocalDate;
import java.util.List;

public record NotificacaoCreateResponseDTO(String id, String email, String assunto, String mensagem,
        LocalDate dataRegistro, String tipo, int totalDestinatarios, List<NotificacaoAnexoDTO> anexos) {
    public NotificacaoCreateResponseDTO(String id, String email, String assunto, String mensagem,
            LocalDate dataRegistro, String tipo, int totalDestinatarios) {
        this(id, email, assunto, mensagem, dataRegistro, tipo, totalDestinatarios, List.of());
    }
}