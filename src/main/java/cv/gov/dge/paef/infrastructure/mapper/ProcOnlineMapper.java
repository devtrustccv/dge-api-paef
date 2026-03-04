package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.domain.proconline.model.ProcOnlineRow;
import cv.gov.dge.paef.infrastructure.PaefTProcOnlineEntity;
import cv.gov.dge.paef.interfaces.dto.ProcOnlineDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// infrastructure/mapper/ProcOnlineMapper.java
@Component
public class ProcOnlineMapper {
    @Value("${link.page.igrp}") String linkPage;
    @Value("${link.page.document_viewer}") String linkDocViewer;
    public ProcOnlineRow toModel(PaefTProcOnlineEntity e ) {
        return ProcOnlineRow.builder()
                .tipoPedidoProcesso(e.getTpProcesso())
                .descricao(e.getDescricao())
                .docUuid(e.getDocClob())
                .linkPage(linkPage.replace("$page$", e.getCodePage()))
                .build();
    }

    public ProcOnlineDTO toDto(ProcOnlineRow m) {
        return ProcOnlineDTO.builder()
                .tipoPedidoProcesso(m.tipoPedidoProcesso())
                .descricao(m.descricao())
                .docUuid(m.docUuid())
                .linkPage(m.linkPage())
                .build();
    }
}
