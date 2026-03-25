package cv.gov.dge.paef.application.documento.service;

import cv.gov.dge.paef.infrastructure.ClobEntity;
import cv.gov.dge.paef.infrastructure.repository.ClobRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.DocumentoBase64DTO;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
@Transactional(readOnly = true)
public class DocumentoService {

    private final ClobRepository clobRepo;

    public DocumentoService(ClobRepository clobRepo) {
        this.clobRepo = clobRepo;
    }

    public ApiResponse<?> getByUuid(String uuid) {

        var doc = clobRepo.findByUuid(uuid).orElse(null);
        if (doc == null) {
            return ApiResponse.fail( "Documento não encontrado", null);
        }

        if (doc.getFile() == null || doc.getFile().length == 0) {
            return ApiResponse.fail("Documento sem conteúdo (file vazio)", null);
        }

        String ext = "pdf";
        if (doc.getName() != null) {
            int idx = doc.getName().lastIndexOf('.');
            if (idx >= 0 && idx < doc.getName().length() - 1) {
                ext = doc.getName().substring(idx + 1);
            }
        }

        String mime = (doc.getMimeType() != null && !doc.getMimeType().isBlank())
                ? doc.getMimeType()
                : "." + ext;

        String b64 = Base64.getEncoder().encodeToString(doc.getFile());

        var payload = DocumentoBase64DTO.builder()
                .mimeType(mime)
                .data(b64)
                .build();

        return ApiResponse.ok( "Documento Carregado com sucesso", payload);
    }
    public ResponseEntity<byte[]> visualizarNoBrowser(String uuid) {

        ClobEntity doc = clobRepo.findByUuid(uuid).orElse(null);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] file = doc.getFile();
        if (file == null || file.length == 0) {
            return ResponseEntity.noContent().build();
        }

        String mime = doc.getMimeType();
        if (mime == null || mime.isBlank()) {
            mime = guessMimeFromName(doc.getName()); // fallback
        }

        String filename = (doc.getName() == null || doc.getName().isBlank())
                ? ("documento-" + uuid)
                : doc.getName();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mime));
        headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());
        headers.setContentLength(file.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
    }

    private String guessMimeFromName(String name) {
        if (name == null) return "application/octet-stream";
        String lower = name.toLowerCase();

        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";

        return "application/octet-stream";
    }
}
