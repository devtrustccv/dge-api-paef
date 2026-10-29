package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.application.generic.service.EmailAnexo;
import cv.gov.dge.paef.infrastructure.DocumentoEntity;
import cv.gov.dge.paef.infrastructure.repository.*;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoAnexoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class NotificacaoAnexoService {
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final long MAX_TOTAL_SIZE = 10L * 1024 * 1024;
    private final DocumentoRepository documentos;
    private final EntidadeRepository entidades;
    private final NotificacaoRepository notificacoes;
    private final NotificacaoMensagemRepository mensagens;
    private final String contextPath;

    public NotificacaoAnexoService(DocumentoRepository documentos, EntidadeRepository entidades,
                                   NotificacaoRepository notificacoes, NotificacaoMensagemRepository mensagens,
                                   @Value("${server.servlet.context-path:}") String contextPath) {
        this.documentos = documentos;
        this.entidades = entidades;
        this.notificacoes = notificacoes;
        this.mensagens = mensagens;
        this.contextPath = contextPath;
    }

    @Transactional
    public List<DocumentoEntity> guardar(String tipoRelacao, String idRelacao, String idNotificacao,
                                        BigDecimal nif, List<MultipartFile> anexos) {
        if (anexos == null || anexos.isEmpty()) return List.of();
        if (anexos.size() > 5) throw new IllegalArgumentException("São permitidos no máximo 5 anexos");
        var docs = new ArrayList<DocumentoEntity>();
        long total = 0;
        for (MultipartFile anexo : anexos) {
            if (anexo == null || anexo.isEmpty()) throw new IllegalArgumentException("Anexo vazio");
            total += anexo.getSize();
            if (anexo.getSize() > MAX_FILE_SIZE || total > MAX_TOTAL_SIZE) {
                throw new IllegalArgumentException("Limite de anexos: 5 MB por ficheiro e 10 MB no total");
            }
            String nome = anexo.getOriginalFilename();
            nome = nome == null ? "anexo" : nome.replace('\\', '/');
            nome = nome.substring(nome.lastIndexOf('/') + 1).replaceAll("[\\p{Cntrl}]", "_");
            if (nome.isBlank()) nome = "anexo";
            if (nome.length() > 255) nome = nome.substring(nome.length() - 255);
            String contentType = anexo.getContentType();
            try {
                var parsed = MediaType.parseMediaType(contentType == null ? "application/octet-stream" : contentType);
                contentType = parsed.isConcrete() ? parsed.toString() : "application/octet-stream";
                if (contentType.length() > 255) contentType = "application/octet-stream";
            } catch (IllegalArgumentException ex) {
                contentType = "application/octet-stream";
            }
            var doc = new DocumentoEntity();
            doc.setId(UUID.randomUUID().toString());
            doc.setTipoRelacao(tipoRelacao);
            doc.setIdRelacao(idRelacao);
            doc.setDescricao(nome);
            doc.setContentType(contentType);
            doc.setDocClob(contextPath + "/notificacoes/" + idNotificacao + "/anexos/" + doc.getId() + "?nif=" + nif);
            try { doc.setFile(anexo.getBytes()); }
            catch (IOException ex) { throw new IllegalArgumentException("Não foi possível ler o anexo", ex); }
            docs.add(doc);
        }
        return documentos.saveAll(docs);
    }

    public List<NotificacaoAnexoDTO> listarMensagem(String idMensagem) {
        return documentos.findByTipoRelacaoAndIdRelacao("NOTIFICACAO_MENSAGEM", idMensagem)
                .stream().map(NotificacaoAnexoService::toDTO).toList();
    }

    public DocumentoEntity obter(String idNotificacao, String idDocumento, BigDecimal nif) {
        var entidade = entidades.findByNif(nif)
                .orElseThrow(() -> new IllegalArgumentException("Entidade não encontrada"));
        var notificacao = notificacoes.findById(idNotificacao)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada"));
        if (!"ENTIDADE".equalsIgnoreCase(notificacao.getTpRelacao())
                || !entidade.getId().equals(notificacao.getTpRelacaoId())) {
            throw new IllegalArgumentException("Notificação não pertence à entidade informada");
        }
        var doc = documentos.findById(idDocumento)
                .orElseThrow(() -> new IllegalArgumentException("Anexo não encontrado"));
        boolean pertence = "NOTIFICACAO".equals(doc.getTipoRelacao()) && idNotificacao.equals(doc.getIdRelacao());
        if ("NOTIFICACAO_MENSAGEM".equals(doc.getTipoRelacao())) {
            pertence = mensagens.findById(doc.getIdRelacao())
                    .map(m -> idNotificacao.equals(m.getIdNotificacao())).orElse(false);
        }
        if (!pertence || doc.getFile() == null) {
            throw new IllegalArgumentException("Anexo não pertence à notificação ou não tem ficheiro disponível");
        }
        return doc;
    }

    public static NotificacaoAnexoDTO toDTO(DocumentoEntity doc) {
        return new NotificacaoAnexoDTO("Anexo", doc.getDocClob(), doc.getId(), doc.getDescricao(),
                doc.getContentType(), doc.getFile() == null ? null : (long) doc.getFile().length);
    }

    public static List<EmailAnexo> paraEmail(List<DocumentoEntity> docs) {
        return docs.stream().map(doc -> new EmailAnexo(doc.getDescricao(), doc.getContentType(), doc.getFile())).toList();
    }
}