package cv.gov.dge.paef.application.notificacao.service;

import java.util.List;
import cv.gov.dge.paef.application.generic.service.EmailAnexo;

public record NotificacaoCriadaEvent(String id, String assunto, String mensagem, List<String> destinatarios, List<EmailAnexo> anexos) {
    public NotificacaoCriadaEvent {
        destinatarios = List.copyOf(destinatarios);
        anexos = List.copyOf(anexos);
    }
    public NotificacaoCriadaEvent(String id, String assunto, String mensagem, List<String> destinatarios) {
        this(id, assunto, mensagem, destinatarios, List.of());
    }
}
