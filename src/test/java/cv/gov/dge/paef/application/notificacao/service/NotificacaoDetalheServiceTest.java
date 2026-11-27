package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.application.domain.service.DominioService;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.infrastructure.TNotificacaoEntity;
import cv.gov.dge.paef.infrastructure.repository.*;
import cv.gov.dge.paef.interfaces.dto.notificacao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacaoDetalheServiceTest {
    private final EntidadeRepository entidades = mock(EntidadeRepository.class);
    private final NotificacaoRepository notificacoes = mock(NotificacaoRepository.class);
    private final DocumentoRepository documentos = mock(DocumentoRepository.class);
    private final NotificacaoRespostaService respostas = mock(NotificacaoRespostaService.class);
    private final NotificacaoDetalheService service = new NotificacaoDetalheService(
            entidades, notificacoes, documentos, mock(DominioService.class), respostas);
    private static final BigDecimal NIF = new BigDecimal("123456789");
    private TNotificacaoEntity notificacao;

    @BeforeEach
    void setUp() {
        var entidade = new EntidadeEntity();
        entidade.setId("ent-1");
        when(entidades.findByNif(NIF)).thenReturn(Optional.of(entidade));
        notificacao = new TNotificacaoEntity();
        notificacao.setId("n-1");
        notificacao.setTpRelacao("ENTIDADE");
        notificacao.setTpRelacaoId("ent-1");
        notificacao.setMensagem("Mensagem original");
        when(notificacoes.findById("n-1")).thenReturn(Optional.of(notificacao));
    }

    @Test
    void includesChronologicalHistoryWithAuthorsDatesAndAttachments() {
        var anexo = new NotificacaoAnexoDTO("Anexo", "/download", "doc-1", "prova.pdf", "application/pdf", 100L);
        var first = new NotificacaoMensagemDTO("m-1", "n-1", "a@example.com", "Primeira resposta",
                LocalDateTime.of(2026, 10, 29, 10, 0), null, List.of(anexo));
        var second = new NotificacaoMensagemDTO("m-2", "n-1", "b@example.com", "Segunda resposta",
                LocalDateTime.of(2026, 10, 29, 11, 0), null, List.of());
        when(respostas.listar("n-1", NIF)).thenReturn(List.of(first, second));
        var result = service.detalhe(NIF, "n-1");
        assertTrue(result.isSuccess());
        var detalhe = (NotificacaoDetalheResponseDTO) result.getData();
        assertEquals("Mensagem original", detalhe.mensagem());
        assertEquals(List.of(first, second), detalhe.respostas());
        assertEquals("prova.pdf", detalhe.respostas().get(0).anexos().get(0).nome());
        verify(respostas).listar("n-1", NIF);
    }

    @Test
    void notificationWithoutRepliesReturnsEmptyList() {
        when(respostas.listar("n-1", NIF)).thenReturn(List.of());
        var detalhe = (NotificacaoDetalheResponseDTO) service.detalhe(NIF, "n-1").getData();
        assertNotNull(detalhe.respostas());
        assertTrue(detalhe.respostas().isEmpty());
    }

    @Test
    void doesNotReadHistoryForNotificationFromAnotherEntity() {
        notificacao.setTpRelacaoId("outra-entidade");
        var result = service.detalhe(NIF, "n-1");
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        verifyNoInteractions(respostas, documentos);
    }
}