package cv.gov.dge.paef.web.notificacao;

import cv.gov.dge.paef.application.notificacao.service.*;
import cv.gov.dge.paef.interfaces.dto.notificacao.*;
import cv.gov.dge.paef.interfaces.error.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotificacaoMultipartTest {
    @Test
    void notificationAcceptsJsonPartAndMultipleFiles() throws Exception {
        var service = mock(NotificacaoCreateService.class);
        when(service.criar(any(), any())).thenReturn(new NotificacaoCreateResponseDTO(
                "n-1", "a@example.com", "Assunto", "Mensagem", LocalDate.now(), "EMAIL", 2));
        var mvc = MockMvcBuilders.standaloneSetup(new NotificacaoController(
                mock(NotificacaoService.class), mock(NotificacaoDetalheService.class), service))
                .setControllerAdvice(new ApiExceptionHandler()).build();
        mvc.perform(multipart("/notificacoes")
                .file(json("notificacao", "{\"nif\":123456789,\"email\":\"a@example.com\",\"assunto\":\"Assunto\",\"mensagem\":\"Mensagem\"}"))
                .file(file("a.pdf")).file(file("b.pdf")))
                .andExpect(status().isCreated());
        verify(service).criar(argThat(dto -> dto.email().equals("a@example.com")),
                argThat(files -> files.size() == 2 && files.get(0).getOriginalFilename().equals("a.pdf")));
        mvc.perform(multipart("/notificacoes").file(json("notificacao", "{}" )).file(file("a.pdf")))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(service);
    }

    @Test
    void replyAcceptsJsonAndFilesAndDownloadReturnsExactBytes() throws Exception {
        var service = mock(NotificacaoRespostaService.class);
        when(service.responder(eq("n-1"), any(), any())).thenReturn(new NotificacaoMensagemDTO(
                "m-1", "n-1", "a@example.com", "Resposta", LocalDateTime.now(), null));
        var downloads = mock(NotificacaoAnexoService.class);
        var doc = new cv.gov.dge.paef.infrastructure.DocumentoEntity();
        doc.setFile(new byte[]{0, 1, 2});
        doc.setDescricao("prova.pdf");
        doc.setContentType("application/pdf");
        when(downloads.obter(eq("n-1"), eq("d-1"), any())).thenReturn(doc);
        var mvc = MockMvcBuilders.standaloneSetup(new NotificacaoRespostaController(service),
                new NotificacaoAnexoController(downloads)).setControllerAdvice(new ApiExceptionHandler()).build();
        mvc.perform(multipart("/notificacoes/n-1/respostas")
                .file(json("resposta", "{\"nif\":123456789,\"email\":\"a@example.com\",\"mensagem\":\"Resposta\"}"))
                .file(file("a.pdf")))
                .andExpect(status().isCreated());
        verify(service).responder(eq("n-1"), argThat(dto -> dto.mensagem().equals("Resposta")),
                argThat(files -> files.size() == 1));
        mvc.perform(get("/notificacoes/n-1/anexos/d-1").param("nif", "123456789"))
                .andExpect(status().isOk()).andExpect(content().bytes(new byte[]{0, 1, 2}))
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    private MockMultipartFile json(String name, String content) {
        return new MockMultipartFile(name, "", "application/json", content.getBytes(StandardCharsets.UTF_8));
    }
    private MockMultipartFile file(String name) {
        return new MockMultipartFile("anexos", name, "application/pdf", new byte[]{0, 1, 2});
    }
}