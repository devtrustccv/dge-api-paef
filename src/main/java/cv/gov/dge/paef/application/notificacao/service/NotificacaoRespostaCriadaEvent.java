package cv.gov.dge.paef.application.notificacao.service;

import java.util.List;
import cv.gov.dge.paef.application.generic.service.EmailAnexo;

public record NotificacaoRespostaCriadaEvent(String idMensagem, String assunto, String mensagem,
                                            List<String> destinatarios, List<EmailAnexo> anexos) {
    public NotificacaoRespostaCriadaEvent {
        destinatarios = List.copyOf(destinatarios);
        anexos = List.copyOf(anexos);
    }
    public NotificacaoRespostaCriadaEvent(String idMensagem, String assunto, String mensagem, List<String> destinatarios) {
        this(idMensagem, assunto, mensagem, destinatarios, List.of());
    }
}
