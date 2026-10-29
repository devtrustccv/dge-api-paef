package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.infrastructure.TNotificacaoEntity;
import cv.gov.dge.paef.infrastructure.repository.AlvaraRepository;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoRepository;
import cv.gov.dge.paef.infrastructure.repository.PaefEmailsRepository;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoCreateRequestDTO;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoCreateResponseDTO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class NotificacaoCreateService {
    private final NotificacaoRepository notificacoes;
    private final EntidadeRepository entidades;
    private final AlvaraRepository alvaras;
    private final PaefEmailsRepository emails;
    private final ApplicationEventPublisher events;
    private final NotificacaoAnexoService anexosService;

    public NotificacaoCreateService(NotificacaoRepository notificacoes, EntidadeRepository entidades,
                                    AlvaraRepository alvaras, PaefEmailsRepository emails,
                                    ApplicationEventPublisher events, NotificacaoAnexoService anexosService) {
        this.notificacoes = notificacoes;
        this.entidades = entidades;
        this.alvaras = alvaras;
        this.emails = emails;
        this.events = events;
        this.anexosService = anexosService;
    }

    @Transactional
    public NotificacaoCreateResponseDTO criar(NotificacaoCreateRequestDTO dto) {
        return criar(dto, java.util.List.of());
    }

    @Transactional
    public NotificacaoCreateResponseDTO criar(NotificacaoCreateRequestDTO dto,
            java.util.List<org.springframework.web.multipart.MultipartFile> anexos) {
        var entidade = entidades.findByNif(dto.nif())
                .orElseThrow(() -> new IllegalArgumentException("Entidade não encontrada para o NIF informado"));
        String idAlvara = optional(dto.idAlvara());
        if (idAlvara != null) {
            var alvara = alvaras.findById(idAlvara)
                    .orElseThrow(() -> new IllegalArgumentException("Alvará não encontrado"));
            if (!entidade.getId().equals(alvara.getIdEntidade())) {
                throw new IllegalArgumentException("Alvará não pertence à entidade informada");
            }
        }

        var destinatarios = NotificacaoDestinatarios.resolve(emails.findEmails());

        var notificacao = new TNotificacaoEntity();
        notificacao.setId(UUID.randomUUID().toString());
        notificacao.setTpRelacao("ENTIDADE");
        notificacao.setTpRelacaoId(entidade.getId());
        notificacao.setEmail(dto.email().trim());
        notificacao.setAssunto(dto.assunto().trim());
        notificacao.setMensagem(dto.mensagem());
        notificacao.setDataRegistro(LocalDate.now());
        notificacao.setTipo("EMAIL");
        notificacao.setIdAlvara(idAlvara);
        notificacao.setIdProcesso(optional(dto.idProcesso()));
        notificacao.setIdPaginaEtapa(optional(dto.idPaginaEtapa()));
        notificacoes.save(notificacao);
        var docs = anexosService.guardar("NOTIFICACAO", notificacao.getId(), notificacao.getId(), dto.nif(), anexos);
        events.publishEvent(new NotificacaoCriadaEvent(notificacao.getId(), notificacao.getAssunto(),
                notificacao.getMensagem(), destinatarios, NotificacaoAnexoService.paraEmail(docs)));
        return new NotificacaoCreateResponseDTO(notificacao.getId(), notificacao.getEmail(),
                notificacao.getAssunto(), notificacao.getMensagem(), notificacao.getDataRegistro(),
                notificacao.getTipo(), destinatarios.size(), docs.stream().map(NotificacaoAnexoService::toDTO).toList());
    }

    private static String optional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
