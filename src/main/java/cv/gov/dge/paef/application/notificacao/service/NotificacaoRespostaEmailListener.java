package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.application.generic.service.EmailService;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoMensagemRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NotificacaoRespostaEmailListener {
    private final EmailService emails;
    private final NotificacaoMensagemRepository mensagens;

    public NotificacaoRespostaEmailListener(EmailService emails, NotificacaoMensagemRepository mensagens) {
        this.emails = emails;
        this.mensagens = mensagens;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void enviar(NotificacaoRespostaCriadaEvent event) {
        var restantes = new AtomicInteger(event.destinatarios().size());
        for (String destinatario : event.destinatarios()) {
            Runnable sucesso = () -> {
                if (restantes.decrementAndGet() == 0) {
                    mensagens.markEnviada(event.idMensagem(), LocalDateTime.now());
                }
            };
            if (event.anexos().isEmpty()) {
                emails.sendText(destinatario, event.assunto(), event.mensagem(), sucesso);
            } else {
                emails.sendText(destinatario, event.assunto(), event.mensagem(), event.anexos(), sucesso);
            }
        }
    }
}
