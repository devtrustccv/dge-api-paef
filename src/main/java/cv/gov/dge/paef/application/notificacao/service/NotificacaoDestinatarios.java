package cv.gov.dge.paef.application.notificacao.service;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.TreeSet;

final class NotificacaoDestinatarios {
    private NotificacaoDestinatarios() {}

    static List<String> resolve(List<String> emails) {
        var destinatarios = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        for (String email : emails) {
            if (!StringUtils.hasText(email)) continue;
            String destinatario = email.trim();
            try {
                new InternetAddress(destinatario, true).validate();
            } catch (AddressException ex) {
                throw new IllegalArgumentException("Existe um email inválido configurado em paef_t_emails");
            }
            destinatarios.add(destinatario);
        }
        if (destinatarios.isEmpty()) {
            throw new IllegalArgumentException("Não existem destinatários configurados em paef_t_emails");
        }
        return List.copyOf(destinatarios);
    }
}
