package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.infrastructure.*;
import cv.gov.dge.paef.infrastructure.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificacaoAnexoServiceTest {
    private final DocumentoRepository documentos = mock(DocumentoRepository.class);
    private final EntidadeRepository entidades = mock(EntidadeRepository.class);
    private final NotificacaoRepository notificacoes = mock(NotificacaoRepository.class);
    private final NotificacaoMensagemRepository mensagens = mock(NotificacaoMensagemRepository.class);
    private final NotificacaoAnexoService service = new NotificacaoAnexoService(
            documentos, entidades, notificacoes, mensagens, "/api");
    private static final BigDecimal NIF = new BigDecimal("123456789");
    private final byte[] bytes = new byte[]{0, 1, 2, -1};

    @BeforeEach
    void setUp() {
        when(documentos.saveAll(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void storesBinaryAndMetadataForBothRelationsAndBuildsDownloadLinks() {
        var upload = new MockMultipartFile("anexos", "C:\\temp\\prova.pdf", "application/pdf", bytes);
        for (String tipo : List.of("NOTIFICACAO", "NOTIFICACAO_MENSAGEM")) {
            var antes = LocalDate.now();
            var docs = service.guardar(tipo, "relacao-1", "notificacao-1", NIF, List.of(upload));
            var doc = docs.get(0);
            assertNotNull(doc.getDataRegisto());
            assertFalse(doc.getDataRegisto().isBefore(antes));
            assertFalse(doc.getDataRegisto().isAfter(LocalDate.now()));
            assertEquals(tipo, doc.getTipoRelacao());
            assertEquals("relacao-1", doc.getIdRelacao());
            assertEquals("prova.pdf", doc.getDescricao());
            assertArrayEquals(bytes, doc.getFile());
            assertEquals("application/pdf", doc.getContentType());
            assertTrue(doc.getDocClob().startsWith("/api/notificacoes/notificacao-1/anexos/"));
            assertTrue(doc.getDocClob().endsWith("?nif=123456789"));
            assertEquals(4, NotificacaoAnexoService.toDTO(doc).tamanho());
            assertArrayEquals(bytes, NotificacaoAnexoService.paraEmail(docs).get(0).conteudo());
        }
    }

    @Test
    void rejectsEmptyTooManyAndOversizedFilesWithoutSaving() {
        var empty = new MockMultipartFile("anexos", new byte[0]);
        assertThrows(IllegalArgumentException.class,
                () -> service.guardar("NOTIFICACAO", "n", "n", NIF, List.of(empty)));
        var upload = new MockMultipartFile("anexos", bytes);
        assertThrows(IllegalArgumentException.class,
                () -> service.guardar("NOTIFICACAO", "n", "n", NIF, java.util.Collections.nCopies(6, upload)));
        var oversized = mock(MultipartFile.class);
        when(oversized.getSize()).thenReturn(6L * 1024 * 1024);
        assertThrows(IllegalArgumentException.class,
                () -> service.guardar("NOTIFICACAO", "n", "n", NIF, List.of(oversized)));
        verify(documentos, never()).saveAll(any());
    }

    @Test
    void unreadableFilePreventsSaving() throws Exception {
        var upload = mock(MultipartFile.class);
        when(upload.getSize()).thenReturn(100L);
        when(upload.getBytes()).thenThrow(new IOException("Unreadable"));
        assertThrows(IllegalArgumentException.class,
                () -> service.guardar("NOTIFICACAO", "n", "n", NIF, List.of(upload)));
        verify(documentos, never()).saveAll(any());
    }

    @Test
    void downloadChecksEntityAndNotificationForReplyAttachments() {
        var ent = new EntidadeEntity();
        ent.setId("ent-1");
        when(entidades.findByNif(NIF)).thenReturn(Optional.of(ent));
        var notif = new TNotificacaoEntity();
        notif.setTpRelacao("ENTIDADE");
        notif.setTpRelacaoId("ent-1");
        when(notificacoes.findById("n-1")).thenReturn(Optional.of(notif));
        var doc = new DocumentoEntity();
        doc.setTipoRelacao("NOTIFICACAO_MENSAGEM");
        doc.setIdRelacao("m-1");
        doc.setFile(bytes);
        when(documentos.findById("d-1")).thenReturn(Optional.of(doc));
        var mensagem = new NotificacaoMensagemEntity();
        mensagem.setIdNotificacao("n-1");
        when(mensagens.findById("m-1")).thenReturn(Optional.of(mensagem));
        assertSame(doc, service.obter("n-1", "d-1", NIF));
        mensagem.setIdNotificacao("outra-notificacao");
        assertThrows(IllegalArgumentException.class, () -> service.obter("n-1", "d-1", NIF));
        notif.setTpRelacaoId("outra-entidade");
        assertThrows(IllegalArgumentException.class, () -> service.obter("n-1", "d-1", NIF));
    }

    @Test
    void noFilesPreservesJsonRequests() {
        assertTrue(service.guardar("NOTIFICACAO", "n", "n", NIF, null).isEmpty());
        verify(documentos, never()).saveAll(any());
    }
}
