package cv.gov.dge.paef.interfaces.dto.notificacao;

import cv.gov.dge.paef.infrastructure.TNotificacaoEntity;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class NotificacaoSpecs {

    private NotificacaoSpecs() {}

    public static Specification<TNotificacaoEntity> byEntidade(String idEntidade) {
        return (root, q, cb) -> cb.and(
                cb.equal(root.get("tpRelacao"), "ENTIDADE"),
                cb.equal(root.get("tpRelacaoId"), idEntidade)
        );
    }

    public static Specification<TNotificacaoEntity> byAlvara(String idAlvara) {
        if (idAlvara == null || idAlvara.isBlank()) return null;
        return (root, q, cb) -> cb.equal(root.get("idAlvara"), idAlvara);
    }

    public static Specification<TNotificacaoEntity> dataEnvioBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return null;
        return (root, q, cb) -> cb.between(root.get("dataEnvio"), start, end);
    }
}