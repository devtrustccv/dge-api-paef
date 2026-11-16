package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.application.generic.service.EmailService;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NotificacaoEmailListener {
    private final EmailService emails;
    private final NotificacaoRepository notificacoes;

    public NotificacaoEmailListener(EmailService emails, NotificacaoRepository notificacoes) {
        this.emails = emails;
        this.notificacoes = notificacoes;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void enviar(NotificacaoCriadaEvent event) {
        var restantes = new AtomicInteger(event.destinatarios().size());
        for (String destinatario : event.destinatarios()) {
            Runnable sucesso = () -> {
                if (restantes.decrementAndGet() == 0) {
                    notificacoes.markEnviada(event.id(), LocalDateTime.now());
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
