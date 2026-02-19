package cv.gov.dge.paef.domain.proconline.model;

import lombok.Builder;

// domain/proconline/model/ProcOnlineRow.java
@Builder
public record ProcOnlineRow(
        String idTpProcesso,
        String tipoPedidoProcesso
) {}
