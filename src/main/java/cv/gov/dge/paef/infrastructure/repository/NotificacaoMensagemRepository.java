package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.NotificacaoMensagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacaoMensagemRepository extends JpaRepository<NotificacaoMensagemEntity, String> {
    List<NotificacaoMensagemEntity> findByIdNotificacaoOrderByDataRegistroAscIdAsc(String idNotificacao);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("update NotificacaoMensagemEntity m set m.dataEnvio = :dataEnvio where m.id = :id")
    int markEnviada(@Param("id") String id, @Param("dataEnvio") LocalDateTime dataEnvio);
}
