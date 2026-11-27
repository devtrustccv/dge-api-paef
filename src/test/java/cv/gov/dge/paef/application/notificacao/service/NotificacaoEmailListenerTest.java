package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.application.generic.service.EmailService;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.event.TransactionalEventListenerFactory;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.TransactionDefinition;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NotificacaoEmailListenerTest {
    @Test
    void sendsOnlyAfterCommitAndMarksDateAfterAllSmtpSuccesses() {
        var emails = mock(EmailService.class);
        var repo = mock(NotificacaoRepository.class);
        try (var context = context(emails, repo)) {
            var tx = new TransactionTemplate(new TestTransactionManager());
            tx.executeWithoutResult(status -> {
                context.publishEvent(event());
                verifyNoInteractions(emails);
            });
            var callbacks = org.mockito.ArgumentCaptor.forClass(Runnable.class);
            verify(emails, times(2)).sendText(anyString(), eq("Assunto"), eq("Mensagem"), callbacks.capture());
            verify(emails).sendText(eq("a@example.com"), anyString(), anyString(), any());
            verify(emails).sendText(eq("b@example.com"), anyString(), anyString(), any());
            callbacks.getAllValues().get(0).run();
            verifyNoInteractions(repo);
            callbacks.getAllValues().get(1).run();
            verify(repo, times(1)).markEnviada(eq("notificacao-1"), any());
        }
    }

    @Test
    void rollbackDoesNotSendEmails() {
        var emails = mock(EmailService.class);
        var repo = mock(NotificacaoRepository.class);
        try (var context = context(emails, repo)) {
            new TransactionTemplate(new TestTransactionManager()).executeWithoutResult(status -> {
                context.publishEvent(event());
                status.setRollbackOnly();
            });
            verifyNoInteractions(emails, repo);
        }
    }

    private NotificacaoCriadaEvent event() {
        return new NotificacaoCriadaEvent("notificacao-1", "Assunto", "Mensagem",
                List.of("a@example.com", "b@example.com"));
    }

    private AnnotationConfigApplicationContext context(EmailService emails, NotificacaoRepository repo) {
        var context = new AnnotationConfigApplicationContext();
        context.registerBean(TransactionalEventListenerFactory.class);
        context.registerBean(NotificacaoEmailListener.class, () -> new NotificacaoEmailListener(emails, repo));
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
        var repo = mock(NotificacaoRepository.class);
        var anexos = List.of(new cv.gov.dge.paef.application.generic.service.EmailAnexo(
                "prova.pdf", "application/pdf", new byte[]{1, 2}));
        new NotificacaoEmailListener(emails, repo).enviar(new NotificacaoCriadaEvent("id-1", "Assunto", "Mensagem",
                List.of("a@example.com", "b@example.com"), anexos));
        verify(emails).sendText(eq("a@example.com"), eq("Assunto"), eq("Mensagem"), eq(anexos), any(Runnable.class));
        verify(emails).sendText(eq("b@example.com"), eq("Assunto"), eq("Mensagem"), eq(anexos), any(Runnable.class));
        verifyNoInteractions(repo);
    }
}
