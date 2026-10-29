package cv.gov.dge.paef.interfaces.dto.notificacao;

import lombok.Builder;

@Builder
public record NotificacaoAnexoDTO(String tipoDocumento, String documento, String id,
                                  String nome, String contentType, Long tamanho) {
    public NotificacaoAnexoDTO(String tipoDocumento, String documento) {
        this(tipoDocumento, documento, null, null, null, null);
    }
}