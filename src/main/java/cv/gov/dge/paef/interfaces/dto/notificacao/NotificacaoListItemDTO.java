package cv.gov.dge.paef.interfaces.dto.notificacao;

import lombok.Builder;

@Builder
public record NotificacaoListItemDTO(
        String id,
        String assunto,
        String dataRegisto,
        String meioEnvio,
        String nProcesso,
        String tipoProcesso,
        String ambito,
        String nrAlvara
) {}
