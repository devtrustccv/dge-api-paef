package cv.gov.dge.paef.interfaces.dto.notificacao;

import lombok.Builder;

@Builder
public record NotificacaoAnexoDTO(
        String tipoDocumento,
        String documento
) {}
