package cv.gov.dge.paef.application.pedido.service;

import cv.gov.dge.paef.application.domain.service.DominioService;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.infrastructure.TPedidoEntity;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.PedidoRepository;
import cv.gov.dge.paef.interfaces.dto.DominioKeyDTO;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProcessosPendentesServiceTest {
    private final PedidoRepository pedidos = mock(PedidoRepository.class);
    private final EntidadeRepository entidades = mock(EntidadeRepository.class);
    private final DominioService dominios = mock(DominioService.class);
    private final PedidoService service = new PedidoService(pedidos, entidades, dominios);
    private final BigDecimal nif = new BigDecimal("123456789");

    @Test
    void listsBothCorrectionStagesForResolvedEntityUsingExistingResponseFields() {
        var entidade = new EntidadeEntity();
        entidade.setId("entidade-1");
        when(entidades.findByNif(nif)).thenReturn(Optional.of(entidade));
        var correcao = TPedidoEntity.builder().id("pedido-1").idProcesso(BigDecimal.ONE)
                .etapaAtual("correcao").descEtapaAtual("Correção").idEtapa("task-1").build();
        var visita = TPedidoEntity.builder().id("pedido-2").idProcesso(BigDecimal.TEN)
                .etapaAtual("correcao_visita").idEtapa("task-2").build();
        when(pedidos.findByIdEntidadeAndEtapaAtualInOrderByDtRegistoDescIdDesc(
                "entidade-1", List.of("correcao", "correcao_visita")))
                .thenReturn(List.of(correcao, visita));
        when(dominios.getDesc(anyString(), nullable(String.class)))
                .thenReturn(DominioKeyDTO.builder().description("").build());
        service.linkDetalhe = "https://example.test/task/";
        var result = service.listarProcessosPendentes(nif);
        assertTrue(result.isSuccess());
        assertEquals(List.of("correcao", "correcao_visita"),
                result.getData().stream().map(p -> p.etapaAtual()).toList());
        assertEquals("pedido-1", result.getData().get(0).idPedido());
        assertEquals("Correção", result.getData().get(0).descEtapaAtual());
        assertNull(result.getData().get(1).descEtapaAtual());
        assertEquals("https://example.test/task/task-1", result.getData().get(0).linkDetalhe());
        verify(pedidos).findByIdEntidadeAndEtapaAtualInOrderByDtRegistoDescIdDesc(
                "entidade-1", List.of("correcao", "correcao_visita"));
        verifyNoMoreInteractions(pedidos);
    }

    @Test
    void unknownEntityDoesNotQueryProcesses() {
        when(entidades.findByNif(nif)).thenReturn(Optional.empty());
        assertFalse(service.listarProcessosPendentes(nif).isSuccess());
        verifyNoInteractions(pedidos);
    }

    @Test
    void returnsEmptyListWhenEntityHasNoCorrections() {
        var entidade = new EntidadeEntity();
        entidade.setId("entidade-1");
        when(entidades.findByNif(nif)).thenReturn(Optional.of(entidade));
        when(pedidos.findByIdEntidadeAndEtapaAtualInOrderByDtRegistoDescIdDesc(
                "entidade-1", List.of("correcao", "correcao_visita"))).thenReturn(List.of());
        var result = service.listarProcessosPendentes(nif);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isEmpty());
    }
}
