package cv.gov.dge.paef.application.generic.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;

@Service
@Log4j2
public class EmailService {
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[a-zA-Z][\\s\\S]*>");

    private final JavaMailSender mailSender;
    private final TaskExecutor emailTaskExecutor;

    @Value("${application.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${application.mail.from:${spring.mail.username}}")
    private String from;

    public EmailService(JavaMailSender mailSender, @Qualifier("emailTaskExecutor") TaskExecutor emailTaskExecutor) {
        this.mailSender = mailSender;
        this.emailTaskExecutor = emailTaskExecutor;
    }

    public void sendText(String to, String subject, String body) {
        sendText(to, subject, body, () -> {});
    }

    public void sendText(String to, String subject, String body, Runnable onSuccess) {
        sendText(to, subject, body, List.of(), onSuccess);
    }

    public void sendText(String to, String subject, String body, List<EmailAnexo> anexos, Runnable onSuccess) {
        send(to, subject, body, isHtml(body), List.copyOf(anexos), onSuccess);
    }

    public void sendHtml(String to, String subject, String body) {
        send(to, subject, body, true, List.of(), () -> {});
    }

    private void send(String to, String subject, String body, boolean html, List<EmailAnexo> anexos, Runnable onSuccess) {
        if (!mailEnabled) {
            log.info("Mail provider disabled. Email to {} with subject '{}' was not sent.", to, subject);
            return;
        }

        if (!StringUtils.hasText(to)) {
            log.warn("Email recipient is required. Email with subject '{}' was not queued.", subject);
            return;
        }

        try {
            emailTaskExecutor.execute(() -> sendNow(to, subject, body, html, anexos, onSuccess));
        } catch (TaskRejectedException e) {
            log.error("Email queue rejected message to {} with subject '{}'.", to, subject, e);
        }
    }

    private void sendNow(String to, String subject, String body, boolean html, List<EmailAnexo> anexos, Runnable onSuccess) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, !anexos.isEmpty(), StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body != null ? body : "", html);
            for (EmailAnexo anexo : anexos) {
                helper.addAttachment(anexo.nome(), new ByteArrayResource(anexo.conteudo()), anexo.contentType());
            }
            mailSender.send(message);
            log.info("Email sent to {} with subject '{}'.", to, subject);
        } catch (MailException | MessagingException e) {
            log.error("Erro ao enviar email para {} com assunto '{}'.", to, subject, e);
            return;
        }
        try {
            onSuccess.run();
        } catch (RuntimeException e) {
            log.error("Email enviado, mas ocorreu um erro ao registar o resultado do envio.", e);
        }
    }

    private static boolean isHtml(String body) {
        return body != null && HTML_TAG_PATTERN.matcher(body).find();
    }
}
