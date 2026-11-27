package cv.gov.dge.paef.web.pedido;

import cv.gov.dge.paef.application.pedido.service.PedidoService;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProcessosPendentesControllerTest {
    @Test
    void requiresEntityNifAndReturnsListEnvelope() throws Exception {
        var service = mock(PedidoService.class);
        var nif = new BigDecimal("123456789");
        when(service.listarProcessosPendentes(nif)).thenReturn(ApiResponse.ok("OK", List.of()));
        var mvc = MockMvcBuilders.standaloneSetup(new ProcessosPendentesController(service)).build();
        mvc.perform(get("/processos_pendentes").param("nif_entidade", nif.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.data").isEmpty());
        mvc.perform(get("/processos_pendentes")).andExpect(status().isBadRequest());
        mvc.perform(get("/processos_pendentes").param("nif_entidade", "abc"))
                .andExpect(status().isBadRequest());
        verify(service).listarProcessosPendentes(nif);
        verifyNoMoreInteractions(service);
    }
}
