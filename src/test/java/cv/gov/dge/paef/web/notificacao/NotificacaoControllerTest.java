package cv.gov.dge.paef.web.notificacao;

import cv.gov.dge.paef.application.notificacao.service.*;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoCreateRequestDTO;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoCreateResponseDTO;
import cv.gov.dge.paef.interfaces.error.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotificacaoControllerTest {
    @Test
    void createsWithSnakeCaseFieldsAndRejectsInvalidEmail() throws Exception {
        var service = mock(NotificacaoCreateService.class);
        when(service.criar(any())).thenReturn(new NotificacaoCreateResponseDTO("id-1", "autor@example.com",
                "Assunto", "Mensagem", LocalDate.now(), "EMAIL", 2));
        var mvc = MockMvcBuilders.standaloneSetup(new NotificacaoController(
                mock(NotificacaoService.class), mock(NotificacaoDetalheService.class), service))
                .setControllerAdvice(new ApiExceptionHandler()).build();
        mvc.perform(post("/notificacoes").contentType(MediaType.APPLICATION_JSON).content("""
                {"nif":123456789,"email":"autor@example.com","assunto":"Assunto","mensagem":"Mensagem",
                 "id_alvara":"alvara-1","id_processo":"processo-1","id_pagina_etapa":"etapa-1"}
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.data.id").value("id-1"));
        var request = org.mockito.ArgumentCaptor.forClass(NotificacaoCreateRequestDTO.class);
        verify(service).criar(request.capture());
        org.junit.jupiter.api.Assertions.assertEquals("alvara-1", request.getValue().idAlvara());
        org.junit.jupiter.api.Assertions.assertEquals("processo-1", request.getValue().idProcesso());
        org.junit.jupiter.api.Assertions.assertEquals("etapa-1", request.getValue().idPaginaEtapa());
        mvc.perform(post("/notificacoes").contentType(MediaType.APPLICATION_JSON).content("""
                {"nif":123456789,"email":"invalid","assunto":"Assunto","mensagem":"Mensagem"}
                """))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/notificacoes").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(service);
    }
}
