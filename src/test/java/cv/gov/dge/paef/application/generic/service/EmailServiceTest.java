package cv.gov.dge.paef.application.generic.service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {
    private JavaMailSender sender;
    private TaskExecutor executor;
    private EmailService service;
    private MimeMessage message;

    @BeforeEach
    void setUp() {
        sender = mock(JavaMailSender.class);
        executor = mock(TaskExecutor.class);
        service = new EmailService(sender, executor);
        ReflectionTestUtils.setField(service, "mailEnabled", true);
        ReflectionTestUtils.setField(service, "from", "sender@example.com");
        message = new MimeMessage(Session.getInstance(new Properties()));
        when(sender.createMimeMessage()).thenReturn(message);
        doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(executor).execute(any(Runnable.class));
    }

    @Test
    void sendsUtf8Text() throws Exception {
        service.sendText("recipient@example.com", "Notificação", "Olá!");
        verify(sender).send(message);
        assertEquals("recipient@example.com", message.getAllRecipients()[0].toString());
        assertEquals("sender@example.com", message.getFrom()[0].toString());
        assertEquals("Notificação", message.getSubject());
        assertEquals("Olá!", message.getContent());
        message.saveChanges();
        assertTrue(message.isMimeType("text/plain"));
        assertTrue(message.getContentType().toLowerCase().contains("utf-8"));
    }

    @Test
    void detectsHtmlInText() throws Exception {
        service.sendText("recipient@example.com", "Assunto", "<p>Olá</p>");
        message.saveChanges();
        assertTrue(message.isMimeType("text/html"));
        verify(sender).send(message);
    }

    @Test
    void supportsExplicitHtmlAndNullBody() throws Exception {
        service.sendHtml("recipient@example.com", "Assunto", null);
        message.saveChanges();
        assertTrue(message.isMimeType("text/html"));
        assertEquals("", message.getContent());
    }

    @Test
    void disabledProviderDoesNotQueue() {
        ReflectionTestUtils.setField(service, "mailEnabled", false);
        service.sendText("recipient@example.com", "Assunto", "Texto");
        verifyNoInteractions(executor, sender);
    }

    @Test
    void blankRecipientDoesNotQueue() {
        service.sendText(" ", "Assunto", "Texto");
        verifyNoInteractions(executor, sender);
    }

    @Test
    void queuesWithoutSendingOnCallerThread() {
        doNothing().when(executor).execute(any(Runnable.class));
        service.sendText("recipient@example.com", "Assunto", "Texto");
        verify(executor).execute(any(Runnable.class));
        verifyNoInteractions(sender);
    }

    @Test
    void handlesRejectedQueue() {
        doThrow(new TaskRejectedException("Full")).when(executor).execute(any(Runnable.class));
        assertDoesNotThrow(() -> service.sendText("recipient@example.com", "Assunto", "Texto"));
        verifyNoInteractions(sender);
    }

    @Test
    void handlesSmtpFailure() {
        doThrow(new MailSendException("Unavailable")).when(sender).send(message);
        assertDoesNotThrow(() -> service.sendText("recipient@example.com", "Assunto", "Texto"));
        verify(sender).send(message);
    }

    @Test
    void callsSuccessOnlyAfterSmtpAcceptsMessage() {
        var success = mock(Runnable.class);
        service.sendText("recipient@example.com", "Assunto", "Texto", success);
        var order = inOrder(sender, success);
        order.verify(sender).createMimeMessage();
        order.verify(sender).send(message);
        order.verify(success).run();
    }

    @Test
    void failedOrDisabledSendDoesNotCallSuccess() {
        var success = mock(Runnable.class);
        doThrow(new MailSendException("Unavailable")).when(sender).send(message);
        service.sendText("recipient@example.com", "Assunto", "Texto", success);
        ReflectionTestUtils.setField(service, "mailEnabled", false);
        service.sendText("recipient@example.com", "Assunto", "Texto", success);
        verifyNoInteractions(success);
    }
    @Test
    void sendsRealMimeAttachmentWithOriginalBytes() throws Exception {
        byte[] bytes = {0, 1, 2, -1};
        service.sendText("recipient@example.com", "Assunto", "<p>Mensagem</p>",
                java.util.List.of(new EmailAnexo("prova.pdf", "application/pdf", bytes)), () -> {});
        message.saveChanges();
        assertTrue(message.isMimeType("multipart/*"));
        var root = (jakarta.mail.Multipart) message.getContent();
        var attachment = root.getBodyPart(root.getCount() - 1);
        assertEquals("prova.pdf", attachment.getFileName());
        assertArrayEquals(bytes, attachment.getInputStream().readAllBytes());
        verify(sender).send(message);
    }
}
