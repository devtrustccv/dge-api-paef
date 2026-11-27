package cv.gov.dge.paef.application.pedido.service;

import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.PedidoPendenteRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.Pedido.PedidoPendenteListItemDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PedidoPendenteService {
    private final EntidadeRepository entidades;
    private final PedidoPendenteRepository pedidos;

    public PedidoPendenteService(EntidadeRepository entidades, PedidoPendenteRepository pedidos) {
        this.entidades = entidades;
        this.pedidos = pedidos;
    }

    public ApiResponse<List<PedidoPendenteListItemDTO>> listar(BigDecimal nifEntidade) {
        var entidade = entidades.findByNif(nifEntidade);
        if (entidade.isEmpty()) {
            return ApiResponse.fail("Entidade não encontrada para o NIF informado", null);
        }
        var data = pedidos.findByIdEntidadeAndDmEstadoOrderByDataPedidoDescIdDesc(
                        entidade.get().getId(), "PENDENTE")
                .stream().map(PedidoPendenteListItemDTO::from).toList();
        return ApiResponse.ok("Lista carregada com sucesso", data);
    }
}
