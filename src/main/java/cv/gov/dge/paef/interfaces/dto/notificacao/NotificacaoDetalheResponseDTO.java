package cv.gov.dge.paef.interfaces.dto.notificacao;

import lombok.Builder;
import java.util.List;

@Builder
public record NotificacaoDetalheResponseDTO(
        String id,
        String assunto,
        String dataEnvio,
        String email,
        String meioEnvio,
        String telemovel,
        String mensagem,
        List<NotificacaoAnexoDTO> anexos
) {}