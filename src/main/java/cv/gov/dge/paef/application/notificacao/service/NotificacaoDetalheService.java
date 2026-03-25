package cv.gov.dge.paef.application.notificacao.service;


import cv.gov.dge.paef.application.domain.service.DominioService;
import cv.gov.dge.paef.helpers.Utils;
import cv.gov.dge.paef.infrastructure.DocumentoEntity;
import cv.gov.dge.paef.infrastructure.repository.DocumentoRepository;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoAnexoDTO;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoDetalheResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class NotificacaoDetalheService {

    private final EntidadeRepository entidadeRepo;
    private final NotificacaoRepository notificacaoRepo;
    private final DocumentoRepository documentoRepo;
    private final DominioService dominioService;
    //private final FileLinkService fileLinkService;

    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public NotificacaoDetalheService(
            EntidadeRepository entidadeRepo,
            NotificacaoRepository notificacaoRepo,
            DocumentoRepository documentoRepo,
            DominioService dominioService
           // FileLinkService fileLinkService
    ) {
        this.entidadeRepo = entidadeRepo;
        this.notificacaoRepo = notificacaoRepo;
        this.documentoRepo = documentoRepo;
        this.dominioService = dominioService;
        //this.fileLinkService = fileLinkService;
    }

    public ApiResponse<?> detalhe(BigDecimal nif, String idNotificacao) {

        var ent = entidadeRepo.findByNif(nif).orElse(null);
        if (ent == null) {
            return ApiResponse.fail( "Entidade não encontrada para o NIF informado", null);
        }

        var notif = notificacaoRepo.findById(idNotificacao).orElse(null);
        if (notif == null) {
            return ApiResponse.fail( "Notificação não encontrada", null);
        }

        // ✅ Segurança/consistência: valida se pertence à entidade
        // Preferência 1: usar tpRelacao + tpRelacaoId (como no legado)
        if (!"ENTIDADE".equalsIgnoreCase(notif.getTpRelacao()) ||
                notif.getTpRelacaoId() == null ||
                !notif.getTpRelacaoId().equals(ent.getId())) {

            return ApiResponse.fail( "Notificação não pertence à entidade informada", null);
        }

        List<DocumentoEntity> docs = documentoRepo.findByTipoRelacaoAndIdRelacao("NOTIFICACAO", notif.getId());

        List<NotificacaoAnexoDTO> anexos = docs.stream().map(doc -> NotificacaoAnexoDTO.builder()
                .tipoDocumento(dominioService.getDesc("TIPO_DOCUMENTO", doc.getDmTipoDocumento()).description())
                .documento(doc.getDocClob()) // equivale ao Core.getLinkFileByUuid
                .build()
        ).toList();

        var dto = NotificacaoDetalheResponseDTO.builder()
                .id(notif.getId())
                .assunto(notif.getAssunto())
                .dataEnvio(notif.getDataEnvio() == null ? null : D.format(notif.getDataEnvio()))
                .email(Utils.nullToEmpty(notif.getEmail()))
                .meioEnvio(Utils.nullToEmpty(notif.getTipo()))
                .telemovel(Utils.nullToEmpty(notif.getTelemovel()))
                .mensagem(Utils.normalizeLinks(Utils.decorateLinks(Utils.nullToEmpty(notif.getMensagem()))))
                .anexos(anexos)
                .build();

        return ApiResponse.ok( "Detalhe notificação Caregado com sucesso", dto);
    }

}