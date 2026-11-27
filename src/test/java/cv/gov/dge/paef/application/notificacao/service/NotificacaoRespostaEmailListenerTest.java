package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.application.generic.service.EmailService;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoMensagemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.event.TransactionalEventListenerFactory;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NotificacaoRespostaEmailListenerTest {
    @Test
    void sendsAfterCommitAndMarksOnlyMessageAfterAllSuccesses() {
        var emails = mock(EmailService.class);
        var mensagens = mock(NotificacaoMensagemRepository.class);
        try (var context = context(emails, mensagens)) {
            new TransactionTemplate(new TestTransactionManager()).executeWithoutResult(status -> {
                context.publishEvent(event());
                verifyNoInteractions(emails, mensagens);
            });
            var callbacks = org.mockito.ArgumentCaptor.forClass(Runnable.class);
            verify(emails, times(2)).sendText(anyString(), eq("Re: Assunto"), eq("Resposta"), callbacks.capture());
            callbacks.getAllValues().get(0).run();
            verifyNoInteractions(mensagens);
            callbacks.getAllValues().get(1).run();
            verify(mensagens, times(1)).markEnviada(eq("mensagem-1"), any());
        }
    }

    @Test
    void rollbackDoesNotSendReply() {
        var emails = mock(EmailService.class);
        var mensagens = mock(NotificacaoMensagemRepository.class);
        try (var context = context(emails, mensagens)) {
            new TransactionTemplate(new TestTransactionManager()).executeWithoutResult(status -> {
                context.publishEvent(event());
                status.setRollbackOnly();
            });
            verifyNoInteractions(emails, mensagens);
        }
    }

    private NotificacaoRespostaCriadaEvent event() {
        return new NotificacaoRespostaCriadaEvent("mensagem-1", "Re: Assunto", "Resposta",
                List.of("a@example.com", "b@example.com"));
    }

    private AnnotationConfigApplicationContext context(EmailService emails, NotificacaoMensagemRepository mensagens) {
        var context = new AnnotationConfigApplicationContext();
        context.registerBean(TransactionalEventListenerFactory.class);
        context.registerBean(NotificacaoRespostaEmailListener.class,
                () -> new NotificacaoRespostaEmailListener(emails, mensagens));
        context.refresh();
        return context;
    }

    private static class TestTransactionManager extends AbstractPlatformTransactionManager {
        @Override protected Object doGetTransaction() { return new Object(); }
        @Override protected void doBegin(Object tx, TransactionDefinition definition) {}
        @Override protected void doCommit(DefaultTransactionStatus status) {}
        @Override protected void doRollback(DefaultTransactionStatus status) {}
    }
    @Test
    void forwardsAttachmentsToEachRecipient() {
        var emails = mock(EmailService.class);
        var repo = mock(NotificacaoMensagemRepository.class);
        var anexos = List.of(new cv.gov.dge.paef.application.generic.service.EmailAnexo(
                "prova.pdf", "application/pdf", new byte[]{1, 2}));
        new NotificacaoRespostaEmailListener(emails, repo).enviar(new NotificacaoRespostaCriadaEvent("id-1", "Assunto", "Mensagem",
                List.of("a@example.com", "b@example.com"), anexos));
        verify(emails).sendText(eq("a@example.com"), eq("Assunto"), eq("Mensagem"), eq(anexos), any(Runnable.class));
        verify(emails).sendText(eq("b@example.com"), eq("Assunto"), eq("Mensagem"), eq(anexos), any(Runnable.class));
        verifyNoInteractions(repo);
    }
}
