// src/main/java/cv/gov/dge/paef/domain/audit/AlteracaoHistoricoService.java
package cv.gov.dge.paef.application.audit;

import cv.gov.dge.paef.infrastructure.LogAlteracaoEntity;
import cv.gov.dge.paef.infrastructure.repository.LogAlteracaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class AlteracaoHistoricoService {

    private final LogAlteracaoRepository repo;

    public AlteracaoHistoricoService(LogAlteracaoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void saveHistorico(
            LinkedHashMap<String, String> before,
            LinkedHashMap<String, String> after,
            String tipoRelacao,
            String idRelacao,
            String referencia,
            String motivo,
            String userEmail
    ) {
        if (before == null || after == null) return;

        // compara por chave (mais robusto que comparar por posição)
        for (Map.Entry<String, String> e : after.entrySet()) {
            String campo = e.getKey();
            String newVal = nvl(e.getValue());
            String oldVal = nvl(before.get(campo));

            if (!Objects.equals(oldVal, newVal)) {
                saveAlteracao(oldVal, newVal, tipoRelacao, idRelacao, campo, referencia, motivo, userEmail);
            }
        }
    }

    private void saveAlteracao(
            String valorAnt,
            String valorAtual,
            String tipoRelacao,
            String idRelacao,
            String campoAlt,
            String referencia,
            String motivo,
            String userEmail
    ) {
        LogAlteracaoEntity log = LogAlteracaoEntity.builder()
                // se o id for UUID string no app:
                .id(UUID.randomUUID().toString())
                .valorAnterior(valorAnt)
                .valorAtual(valorAtual)
                .tipoRelacao(tipoRelacao)
                .idRelacao(idRelacao)
                .campoAlt(campoAlt)
                .dataRegisto(LocalDate.now())
                .userEmail(userEmail)
                .referencia(referencia)
                .motivo(motivo)
                .build();
        repo.save(log);
    }

    private String nvl(String s) { return s == null ? "" : s; }
}
