package cv.gov.dge.paef.interfaces.dto.Pedido;

import cv.gov.dge.paef.infrastructure.TPedidoPendenteEntity;
import java.time.LocalDateTime;

public record PedidoPendenteListItemDTO(
        Integer id, LocalDateTime dataPedido, String dmEstado,
        String processType, String processTypeName, String taskCod, String taskName,
        String userRegisto, String idAlvara, String idEntidade,
        Boolean possuiFormOutCatalog, String faseAtual, String idPedido,
        String obs, Boolean possuiFormDistancia) {
    public static PedidoPendenteListItemDTO from(TPedidoPendenteEntity p) {
        return new PedidoPendenteListItemDTO(p.getId(), p.getDataPedido(), p.getDmEstado(),
                p.getProcessType(), p.getProcessTypeName(), p.getTaskCod(), p.getTaskName(),
                p.getUserRegisto(), p.getIdAlvara(), p.getIdEntidade(),
                p.getPossuiFormOutCatalog(), p.getFaseAtual(), p.getIdPedido(),
                p.getObs(), p.getPossuiFormDistancia());
    }
}
