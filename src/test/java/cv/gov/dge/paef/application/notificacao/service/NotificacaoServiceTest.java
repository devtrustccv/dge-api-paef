package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.infrastructure.repository.AlvaraRepository;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoRepository;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoRow;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacaoServiceTest {
    private final NotificacaoRepository repo = mock(NotificacaoRepository.class);
    private final EntidadeRepository entidades = mock(EntidadeRepository.class);
    private final NotificacaoService service = new NotificacaoService(repo, entidades, mock(AlvaraRepository.class));

    private void prepareRows() {
        var entidade = new EntidadeEntity();
        entidade.setId("entidade-1");
        when(entidades.findByNif(BigDecimal.ONE)).thenReturn(Optional.of(entidade));
        when(repo.findNotificacoes("entidade-1", null))
                .thenReturn(Collections.nCopies(101, mock(NotificacaoRow.class)));
    }

    @Test
    void omittedLimitDefaultsToOneHundred() {
        prepareRows();
        var response = service.listar(BigDecimal.ONE, null, null, null);
        assertTrue(response.isSuccess());
        assertEquals(100, ((List<?>) response.getData()).size());
    }

    @Test
    void explicitLimitIsPreserved() {
        prepareRows();
        var response = service.listar(BigDecimal.ONE, null, null, 2);
        assertEquals(2, ((List<?>) response.getData()).size());
    }

    @Test
    void zeroLimitReturnsEmptyList() {
        prepareRows();
        var response = service.listar(BigDecimal.ONE, null, null, 0);
        assertTrue(((List<?>) response.getData()).isEmpty());
    }
}
