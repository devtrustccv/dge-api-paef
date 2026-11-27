package cv.gov.dge.paef.web.pedido;

import cv.gov.dge.paef.application.pedido.service.PedidoPendenteService;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PedidoPendenteControllerTest {
    @Test
    void requiresNifAndReturnsPendingListEnvelope() throws Exception {
        var service = mock(PedidoPendenteService.class);
        var nif = new BigDecimal("123456789");
        when(service.listar(nif)).thenReturn(ApiResponse.ok("Lista carregada com sucesso", List.of()));
        var mvc = MockMvcBuilders.standaloneSetup(new PedidoPendenteController(service)).build();
        mvc.perform(get("/pedidos-pendentes").param("nif_entidade", nif.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.data").isEmpty());
        mvc.perform(get("/pedidos-pendentes")).andExpect(status().isBadRequest());
        mvc.perform(get("/pedidos-pendentes").param("nif_entidade", "abc"))
                .andExpect(status().isBadRequest());
        verify(service).listar(nif);
        verifyNoMoreInteractions(service);
    }
}
