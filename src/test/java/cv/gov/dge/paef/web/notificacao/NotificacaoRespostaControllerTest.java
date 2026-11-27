package cv.gov.dge.paef.web.notificacao;

import cv.gov.dge.paef.application.notificacao.service.NotificacaoRespostaService;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoMensagemDTO;
import cv.gov.dge.paef.interfaces.error.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotificacaoRespostaControllerTest {
    @Test
    void createsReplyAndListsHistoryWithValidation() throws Exception {
        var service = mock(NotificacaoRespostaService.class);
        var mensagem = new NotificacaoMensagemDTO("mensagem-1", "notificacao-1", "autor@example.com",
                "Resposta", LocalDateTime.now(), null);
        when(service.responder(eq("notificacao-1"), any())).thenReturn(mensagem);
        when(service.listar("notificacao-1", new BigDecimal("123456789"))).thenReturn(List.of(mensagem));
        var mvc = MockMvcBuilders.standaloneSetup(new NotificacaoRespostaController(service))
                .setControllerAdvice(new ApiExceptionHandler()).build();
        mvc.perform(post("/notificacoes/notificacao-1/respostas").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nif":123456789,"email":"autor@example.com","mensagem":"Resposta"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.data.id").value("mensagem-1"));
        mvc.perform(get("/notificacoes/notificacao-1/mensagens").param("nif", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.data[0].mensagem").value("Resposta"));
        mvc.perform(post("/notificacoes/notificacao-1/respostas").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/notificacoes/notificacao-1/respostas").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nif":123456789,"email":"invalid","mensagem":"Resposta"}
                        """))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/notificacoes/notificacao-1/mensagens"))
                .andExpect(status().isBadRequest());
        verify(service, times(1)).responder(eq("notificacao-1"), any());
        verify(service, times(1)).listar("notificacao-1", new BigDecimal("123456789"));
        verifyNoMoreInteractions(service);
    }
}
