package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.infrastructure.NotificacaoMensagemEntity;
import cv.gov.dge.paef.infrastructure.TNotificacaoEntity;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoRepository;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoMensagemRepository;
import cv.gov.dge.paef.infrastructure.repository.PaefEmailsRepository;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoMensagemDTO;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoRespostaRequestDTO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class NotificacaoRespostaService {
    private final NotificacaoRepository notificacoes;
    private final NotificacaoMensagemRepository mensagens;
    private final EntidadeRepository entidades;
    private final PaefEmailsRepository emails;
    private final ApplicationEventPublisher events;
    private final NotificacaoAnexoService anexosService;

    public NotificacaoRespostaService(NotificacaoRepository notificacoes, NotificacaoMensagemRepository mensagens,
                                     EntidadeRepository entidades, PaefEmailsRepository emails,
                                     ApplicationEventPublisher events, NotificacaoAnexoService anexosService) {
        this.notificacoes = notificacoes;
        this.mensagens = mensagens;
        this.entidades = entidades;
        this.emails = emails;
        this.events = events;
        this.anexosService = anexosService;
    }

    @Transactional
    public NotificacaoMensagemDTO responder(String idNotificacao, NotificacaoRespostaRequestDTO dto) {
        return responder(idNotificacao, dto, List.of());
    }

    @Transactional
    public NotificacaoMensagemDTO responder(String idNotificacao, NotificacaoRespostaRequestDTO dto,
            List<org.springframework.web.multipart.MultipartFile> anexos) {
        var notificacao = validarNotificacao(idNotificacao, dto.nif());
        var destinatarios = NotificacaoDestinatarios.resolve(emails.findEmails());
        var mensagem = new NotificacaoMensagemEntity();
        mensagem.setId(UUID.randomUUID().toString());
        mensagem.setIdNotificacao(notificacao.getId());
        mensagem.setEmail(dto.email().trim());
        mensagem.setMensagem(dto.mensagem());
        mensagem.setDataRegistro(LocalDateTime.now());
        mensagens.save(mensagem);
        var docs = anexosService.guardar("NOTIFICACAO_MENSAGEM", mensagem.getId(), idNotificacao, dto.nif(), anexos);
        String assunto = notificacao.getAssunto() == null ? "Notificação " + notificacao.getId()
                : notificacao.getAssunto();
        events.publishEvent(new NotificacaoRespostaCriadaEvent(mensagem.getId(), "Re: " + assunto,
                mensagem.getMensagem(), destinatarios, NotificacaoAnexoService.paraEmail(docs)));
        return toDTO(mensagem, docs.stream().map(NotificacaoAnexoService::toDTO).toList());
    }

    public List<NotificacaoMensagemDTO> listar(String idNotificacao, BigDecimal nif) {
        validarNotificacao(idNotificacao, nif);
        return mensagens.findByIdNotificacaoOrderByDataRegistroAscIdAsc(idNotificacao)
                .stream().map(m -> toDTO(m, anexosService.listarMensagem(m.getId()))).toList();
    }

    private TNotificacaoEntity validarNotificacao(String id, BigDecimal nif) {
        var entidade = entidades.findByNif(nif)
                .orElseThrow(() -> new IllegalArgumentException("Entidade não encontrada para o NIF informado"));
        var notificacao = notificacoes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada"));
        if (!"ENTIDADE".equalsIgnoreCase(notificacao.getTpRelacao())
                || !entidade.getId().equals(notificacao.getTpRelacaoId())) {
            throw new IllegalArgumentException("Notificação não pertence à entidade informada");
        }
        return notificacao;
    }

    private static NotificacaoMensagemDTO toDTO(NotificacaoMensagemEntity mensagem, List<cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoAnexoDTO> anexos) {
        return new NotificacaoMensagemDTO(mensagem.getId(), mensagem.getIdNotificacao(), mensagem.getEmail(),
                mensagem.getMensagem(), mensagem.getDataRegistro(), mensagem.getDataEnvio(), anexos);
    }
}
