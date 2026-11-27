package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.infrastructure.*;
import cv.gov.dge.paef.infrastructure.repository.*;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoRespostaRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificacaoRespostaServiceTest {
    private final NotificacaoRepository notificacoes = mock(NotificacaoRepository.class);
    private final NotificacaoMensagemRepository mensagens = mock(NotificacaoMensagemRepository.class);
    private final EntidadeRepository entidades = mock(EntidadeRepository.class);
    private final PaefEmailsRepository emails = mock(PaefEmailsRepository.class);
    private final NotificacaoAnexoService anexosService = mock(NotificacaoAnexoService.class);
    private final ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
    private final NotificacaoRespostaService service = new NotificacaoRespostaService(
            notificacoes, mensagens, entidades, emails, events, anexosService);
    private static final BigDecimal NIF = new BigDecimal("123456789");
    private final NotificacaoRespostaRequestDTO request = new NotificacaoRespostaRequestDTO(
            NIF, "autor@example.com", "<p>Resposta</p>");
    private TNotificacaoEntity notificacao;

    @BeforeEach
    void setUp() {
        when(anexosService.guardar(any(), any(), any(), any(), any())).thenReturn(List.of());
        var entidade = new EntidadeEntity();
        entidade.setId("entidade-1");
        when(entidades.findByNif(NIF)).thenReturn(Optional.of(entidade));
        notificacao = new TNotificacaoEntity();
        notificacao.setId("notificacao-1");
        notificacao.setTpRelacao("ENTIDADE");
        notificacao.setTpRelacaoId("entidade-1");
        notificacao.setAssunto("Assunto original");
        notificacao.setMensagem("Mensagem original");
        when(notificacoes.findById("notificacao-1")).thenReturn(Optional.of(notificacao));
    }

    @Test
    void savesOneMessageLinkedToOriginalAndQueuesAllConfiguredRecipients() {
        when(emails.findEmails()).thenReturn(List.of("a@example.com", "A@example.com", "b@example.com"));
        var result = service.responder("notificacao-1", request);
        var saved = org.mockito.ArgumentCaptor.forClass(NotificacaoMensagemEntity.class);
        verify(mensagens, times(1)).save(saved.capture());
        assertEquals("notificacao-1", saved.getValue().getIdNotificacao());
        assertEquals(result.id(), saved.getValue().getId());
        assertEquals("autor@example.com", result.email());
        assertEquals("<p>Resposta</p>", result.mensagem());
        assertNotNull(result.dataRegistro());
        assertNull(result.dataEnvio());
        assertEquals("Mensagem original", notificacao.getMensagem());
        verify(notificacoes, never()).save(any());
        var event = org.mockito.ArgumentCaptor.forClass(NotificacaoRespostaCriadaEvent.class);
        verify(events).publishEvent(event.capture());
        assertEquals(result.id(), event.getValue().idMensagem());
        assertEquals("Re: Assunto original", event.getValue().assunto());
        assertEquals(2, event.getValue().destinatarios().size());
        assertFalse(event.getValue().destinatarios().contains(request.email()));
    }

    @Test
    void rejectsMissingNotificationBeforeWritingOrSending() {
        when(notificacoes.findById("notificacao-1")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.responder("notificacao-1", request));
        verifyNoInteractions(mensagens, emails, events, anexosService);
    }

    @Test
    void rejectsOtherEntityForBothReplyAndHistory() {
        notificacao.setTpRelacaoId("outra-entidade");
        assertThrows(IllegalArgumentException.class, () -> service.responder("notificacao-1", request));
        assertThrows(IllegalArgumentException.class, () -> service.listar("notificacao-1", NIF));
        verifyNoInteractions(mensagens, emails, events, anexosService);
    }

    @Test
    void rejectsUnknownEntity() {
        when(entidades.findByNif(NIF)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.responder("notificacao-1", request));
        verifyNoInteractions(mensagens, emails, events, anexosService);
    }

    @Test
    void invalidConfigurationAndPersistenceFailureDoNotPublish() {
        when(emails.findEmails()).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, () -> service.responder("notificacao-1", request));
        verify(mensagens, never()).save(any());
        when(emails.findEmails()).thenReturn(List.of("a@example.com"));
        when(mensagens.save(any())).thenThrow(new IllegalStateException("Database unavailable"));
        assertThrows(IllegalStateException.class, () -> service.responder("notificacao-1", request));
        verifyNoInteractions(events);
    }

    @Test
    void returnsStoredHistoryUsingChronologicalQuery() {
        var mensagem = new NotificacaoMensagemEntity();
        mensagem.setId("mensagem-1");
        mensagem.setIdNotificacao("notificacao-1");
        mensagem.setEmail("autor@example.com");
        mensagem.setMensagem("Resposta");
        mensagem.setDataRegistro(LocalDateTime.now());
        mensagem.setDataEnvio(LocalDateTime.now());
        when(mensagens.findByIdNotificacaoOrderByDataRegistroAscIdAsc("notificacao-1"))
                .thenReturn(List.of(mensagem));
        var result = service.listar("notificacao-1", NIF);
        assertEquals(1, result.size());
        assertEquals("mensagem-1", result.get(0).id());
        assertNotNull(result.get(0).dataEnvio());
        verify(anexosService).listarMensagem("mensagem-1");
        verifyNoInteractions(emails, events);
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
        var result = service.responder("notificacao-1", request, files);
        verify(anexosService).guardar(org.mockito.ArgumentMatchers.eq("NOTIFICACAO_MENSAGEM"), any(), any(), any(), org.mockito.ArgumentMatchers.eq(files));
        var event = org.mockito.ArgumentCaptor.forClass(NotificacaoRespostaCriadaEvent.class);
        verify(events).publishEvent(event.capture());
        assertEquals("prova.pdf", event.getValue().anexos().get(0).nome());
        assertArrayEquals(new byte[]{1, 2}, event.getValue().anexos().get(0).conteudo());
        assertEquals("doc-1", result.anexos().get(0).id());
    }
}
