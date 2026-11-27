package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.infrastructure.TNotificacaoEntity;
import cv.gov.dge.paef.infrastructure.repository.*;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoCreateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificacaoCreateServiceTest {
    private final NotificacaoRepository notificacoes = mock(NotificacaoRepository.class);
    private final EntidadeRepository entidades = mock(EntidadeRepository.class);
    private final AlvaraRepository alvaras = mock(AlvaraRepository.class);
    private final PaefEmailsRepository emails = mock(PaefEmailsRepository.class);
    private final NotificacaoAnexoService anexosService = mock(NotificacaoAnexoService.class);
    private final ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
    private final NotificacaoCreateService service = new NotificacaoCreateService(
            notificacoes, entidades, alvaras, emails, events, anexosService);
    private static final BigDecimal NIF = new BigDecimal("123456789");

    private NotificacaoCreateRequestDTO request(String alvara) {
        return new NotificacaoCreateRequestDTO(NIF, "autor@example.com", "Assunto", "<p>Mensagem</p>",
                alvara, "processo-1", "etapa-1");
    }

    @BeforeEach
    void setUp() {
        when(anexosService.guardar(any(), any(), any(), any(), any())).thenReturn(List.of());
        var entidade = new EntidadeEntity();
        entidade.setId("entidade-1");
        when(entidades.findByNif(NIF)).thenReturn(Optional.of(entidade));
    }

    @Test
    void savesOneNotificationAndPublishesAllDistinctConfiguredRecipients() {
        when(emails.findEmails()).thenReturn(Arrays.asList(" tecnico@example.com ",
                "TECNICO@example.com", "outro@example.com", " ", null));
        var response = service.criar(request(null));
        var saved = org.mockito.ArgumentCaptor.forClass(TNotificacaoEntity.class);
        verify(notificacoes, times(1)).save(saved.capture());
        assertEquals(response.id(), saved.getValue().getId());
        assertEquals("autor@example.com", saved.getValue().getEmail());
        assertEquals("ENTIDADE", saved.getValue().getTpRelacao());
        assertEquals("entidade-1", saved.getValue().getTpRelacaoId());
        assertEquals("EMAIL", saved.getValue().getTipo());
        assertEquals("processo-1", saved.getValue().getIdProcesso());
        assertEquals("etapa-1", saved.getValue().getIdPaginaEtapa());
        assertNotNull(saved.getValue().getDataRegistro());
        assertNull(saved.getValue().getDataEnvio());
        assertEquals(2, response.totalDestinatarios());
        var event = org.mockito.ArgumentCaptor.forClass(NotificacaoCriadaEvent.class);
        verify(events).publishEvent(event.capture());
        assertEquals(response.id(), event.getValue().id());
        assertEquals(2, event.getValue().destinatarios().size());
        assertFalse(event.getValue().destinatarios().contains("autor@example.com"));
        assertEquals("<p>Mensagem</p>", event.getValue().mensagem());
    }

    @Test
    void rejectsUnknownEntityBeforeSaving() {
        when(entidades.findByNif(NIF)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.criar(request(null)));
        verifyNoInteractions(notificacoes, emails, events, anexosService);
    }

    @Test
    void rejectsAlvaraFromAnotherEntity() {
        var alvara = new AlvaraEntity();
        alvara.setIdEntidade("outra-entidade");
        when(alvaras.findById("alvara-1")).thenReturn(Optional.of(alvara));
        assertThrows(IllegalArgumentException.class, () -> service.criar(request("alvara-1")));
        verifyNoInteractions(notificacoes, emails, events, anexosService);
    }

    @Test
    void rejectsEmptyOrInvalidConfigurationBeforeSaving() {
        when(emails.findEmails()).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, () -> service.criar(request(null)));
        when(emails.findEmails()).thenReturn(List.of("invalid-address"));
        assertThrows(IllegalArgumentException.class, () -> service.criar(request(null)));
        verifyNoInteractions(notificacoes, events);
    }

    @Test
    void doesNotPublishWhenPersistenceFails() {
        when(emails.findEmails()).thenReturn(List.of("tecnico@example.com"));
        when(notificacoes.save(any())).thenThrow(new IllegalStateException("Database unavailable"));
        assertThrows(IllegalStateException.class, () -> service.criar(request(null)));
        verifyNoInteractions(events);
    }
    @Test
    void passesSavedAttachmentsToEmailEventAndResponse() {
        when(emails.findEmails()).thenReturn(List.of("tecnico@example.com"));
        var file = new org.springframework.mock.web.MockMultipartFile("anexos", "prova.pdf", "application/pdf", new byte[]{1, 2});
        var files = List.<org.springframework.web.multipart.MultipartFile>of(file);
        var doc = new cv.gov.dge.paef.infrastructure.DocumentoEntity();
        doc.setId("doc-1");
        doc.setDescricao("prova.pdf");
        doc.setContentType("application/pdf");
        doc.setFile(new byte[]{1, 2});
        when(anexosService.guardar(any(), any(), any(), any(), any())).thenReturn(List.of(doc));
        var result = service.criar(request(null), files);
        verify(anexosService).guardar(org.mockito.ArgumentMatchers.eq("NOTIFICACAO"), any(), any(), any(), org.mockito.ArgumentMatchers.eq(files));
        var event = org.mockito.ArgumentCaptor.forClass(NotificacaoCriadaEvent.class);
        verify(events).publishEvent(event.capture());
        assertEquals("prova.pdf", event.getValue().anexos().get(0).nome());
        assertArrayEquals(new byte[]{1, 2}, event.getValue().anexos().get(0).conteudo());
        assertEquals("doc-1", result.anexos().get(0).id());
    }
}
