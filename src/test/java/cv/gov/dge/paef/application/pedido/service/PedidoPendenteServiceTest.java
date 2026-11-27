package cv.gov.dge.paef.application.pedido.service;

import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.infrastructure.TPedidoPendenteEntity;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.PedidoPendenteRepository;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoPendenteServiceTest {
    private final EntidadeRepository entidades = mock(EntidadeRepository.class);
    private final PedidoPendenteRepository pedidos = mock(PedidoPendenteRepository.class);
    private final PedidoPendenteService service = new PedidoPendenteService(entidades, pedidos);
    private final BigDecimal nif = new BigDecimal("123456789");

    @Test
    void resolvesEntityAndQueriesOnlyItsPendingRequests() {
        var entidade = new EntidadeEntity();
        entidade.setId("entidade-1");
        when(entidades.findByNif(nif)).thenReturn(Optional.of(entidade));
        var pedido = new TPedidoPendenteEntity();
        pedido.setId(42);
        pedido.setDmEstado("PENDENTE");
        pedido.setIdEntidade("entidade-1");
        when(pedidos.findByIdEntidadeAndDmEstadoOrderByDataPedidoDescIdDesc("entidade-1", "PENDENTE"))
                .thenReturn(List.of(pedido));
        var result = service.listar(nif);
        assertTrue(result.isSuccess());
        assertEquals(42, result.getData().get(0).id());
        assertEquals("entidade-1", result.getData().get(0).idEntidade());
        verify(pedidos).findByIdEntidadeAndDmEstadoOrderByDataPedidoDescIdDesc("entidade-1", "PENDENTE");
    }

    @Test
    void unknownEntityDoesNotQueryRequests() {
        when(entidades.findByNif(nif)).thenReturn(Optional.empty());
        assertFalse(service.listar(nif).isSuccess());
        verifyNoInteractions(pedidos);
    }

    @Test
    void entityWithoutPendingRequestsReturnsEmptyList() {
        var entidade = new EntidadeEntity();
        entidade.setId("entidade-2");
        when(entidades.findByNif(nif)).thenReturn(Optional.of(entidade));
        when(pedidos.findByIdEntidadeAndDmEstadoOrderByDataPedidoDescIdDesc("entidade-2", "PENDENTE"))
                .thenReturn(List.of());
        var result = service.listar(nif);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isEmpty());
    }
}
