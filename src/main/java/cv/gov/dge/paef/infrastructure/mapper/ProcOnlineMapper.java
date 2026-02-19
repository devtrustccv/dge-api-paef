package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.domain.proconline.model.ProcOnlineRow;
import cv.gov.dge.paef.infrastructure.PaefTProcOnlineEntity;
import cv.gov.dge.paef.interfaces.dto.ProcOnlineDTO;
import org.springframework.stereotype.Component;

// infrastructure/mapper/ProcOnlineMapper.java
@Component
public class ProcOnlineMapper {

    public ProcOnlineRow toModel(PaefTProcOnlineEntity e, String processoNome) {
        return ProcOnlineRow.builder()
                .idTpProcesso(e.getIdTpProcesso())
                .tipoPedidoProcesso(processoNome)
                .build();
    }

    public ProcOnlineDTO toDto(ProcOnlineRow m) {
        return ProcOnlineDTO.builder()
                .idTpProcesso(m.idTpProcesso())
                .tipoPedidoProcesso(m.tipoPedidoProcesso())
                .build();
    }
}
