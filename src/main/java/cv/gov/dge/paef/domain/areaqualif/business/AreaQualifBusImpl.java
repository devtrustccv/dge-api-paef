package cv.gov.dge.paef.domain.areaqualif.business;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.application.AreaQualif.dto.RevogacaoResult;
import cv.gov.dge.paef.application.AreaQualif.service.AreaQualifService;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.infrastructure.mapper.AreaQualifMapper;
import cv.gov.dge.paef.infrastructure.repository.AreaQualifRepository;
import cv.gov.dge.paef.infrastructure.repository.FormacaoRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AreaQualifBusImpl implements AreaQualifBus {

    private final AreaQualifRepository repo;
    private final FormacaoRepository formacaoRepository;


    public AreaQualifEntity save(AreaQualifEntity entity) {
        return repo.save(entity);
    }

    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return repo.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsBySiglaCodigo(String sigla) {
        return repo.existsBySiglaCodigo(sigla);
    }

    @Transactional(readOnly = true)
    public AreaQualifEntity findFamilyByCode(String codigoFamilia) {
        return repo.findBySiglaCodigoIgnoreCaseAndIdPaiIsNull(codigoFamilia).orElse(null);
    }

    /** Cria família raiz (id_pai=NULL) com mínimo de campos. */
    public AreaQualifEntity createFamily(String codigoFamilia, String denominacaoFamilia) {
        String descricao = (denominacaoFamilia != null && !denominacaoFamilia.isBlank())
                ? denominacaoFamilia : codigoFamilia;

        AreaQualifEntity familia = AreaQualifEntity.builder()
                .siglaCodigo(codigoFamilia)
                .descricao(descricao)
                .estado("A")
                .build();
        // id_pai permanece null para marcar como família
        return repo.save(familia);
    }

    /**
     * Idempotente sob concorrência se usares o índice único (veja Flyway abaixo).
     * Em corrida, se outro request criar primeiro, apanhamos a DataIntegrityViolation e re-buscamos.
     */
    public AreaQualifEntity findOrCreateFamily(String codigoFamilia, String denominacaoFamilia) {
        AreaQualifEntity existente = findFamilyByCode(codigoFamilia);
        if (existente != null) return existente;
        try {
            return createFamily(codigoFamilia, denominacaoFamilia);
        } catch (DataIntegrityViolationException e) {
            // outro nó criou no meio do caminho → ler de novo
            AreaQualifEntity retry = findFamilyByCode(codigoFamilia);
            if (retry != null) return retry;
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public boolean existsQualification(String codigoQualif, String versao) {
        return repo.existsQualification(codigoQualif, versao);
    }


        @Override
        public AreaQualifEntity revoke(AreaQualifDTO dto) {
            AreaQualifEntity entity = repo
                    .findBySiglaCodigoAndVersao(dto.getCodigoQualif(), dto.getVersao())
                    .orElseThrow(() -> new RuntimeException(
                            "Qualificação não encontrada para código "
                                    + dto.getCodigoQualif() + " e versão " + dto.getVersao()
                    ));

            entity.setEstado("I");
            entity.setRvcc(false);
            return repo.save(entity);
        }

        @Override
        public boolean findExisting(String codigoQualif, String versao) {
            return repo.findBySiglaCodigoAndVersao(codigoQualif, versao).isPresent();
        }

    @Override
    public AreaQualifEntity markAsRvcc(AreaQualifDTO dto) {

        AreaQualifEntity entity = repo
                .findBySiglaCodigoAndVersao(dto.getCodigoQualif(), dto.getVersao())
                .orElseThrow(() -> new RuntimeException(
                        "Qualificação não encontrada para código "
                                + dto.getCodigoQualif() + " e versão " + dto.getVersao()
                ));

        entity.setRvcc(true);

        return repo.save(entity);
    }
    @Override
    public Optional<AreaQualifEntity> findBySiglaCodigoAndVersao(String siglaCodigo, String versao) {
        return repo.findBySiglaCodigoAndVersao(siglaCodigo, versao);
    }
    @Override
    @Transactional
    public RevogacaoResult revokeAndReplace(AreaQualifDTO dto) {

        AreaQualifEntity qualifRevogada = repo
                .findBySiglaCodigoAndVersao(dto.getCodigoQualif(), dto.getVersao())
                .orElse(null);

        if (qualifRevogada == null) {
            return new RevogacaoResult(false, 0, 0);
        }

        // 1) revoga qualificação
        qualifRevogada.setEstado("I");
        qualifRevogada.setRvcc(false); // opcional, se fizer sentido no teu domínio
        repo.save(qualifRevogada);

        // 2) buscar acreditações/formações ligadas à qualificação revogada
        List<FormacaoEntity> formacoesAntigas =
                formacaoRepository.findByIdQualificacao(qualifRevogada.getId());

        int inativadas = 0;
        int pendentesCriadas = 0;

        // 3) inativar acreditações antigas
        for (FormacaoEntity formacao : formacoesAntigas) {
            if (!"I".equals(formacao.getDmEstado())) {
                formacao.setDmEstado("I");
                formacaoRepository.save(formacao);
                inativadas++;
            }
        }

        // 4) tratar novas qualificações
        List<AreaQualifDTO> novas = dto.getNovasQualificacoes();

        if (novas != null && !novas.isEmpty()) {

            // deduplicar por codigo + versao
            Map<String, AreaQualifDTO> unicas = new LinkedHashMap<>();
            for (AreaQualifDTO nq : novas) {
                String chave = nq.getCodigoQualif() + "|" + nq.getVersao();
                unicas.putIfAbsent(chave, nq);
            }

            for (AreaQualifDTO novaDto : unicas.values()) {

                // garantir que a nova qualificação existe
                AreaQualifEntity novaQualif = findOrCreateQualification(novaDto);

                // para cada acreditação antiga, criar uma nova pendente
                for (FormacaoEntity antiga : formacoesAntigas) {

                    boolean jaExiste = formacaoRepository.existsByIdAlvaraAndIdQualificacaoAndDmEstado(
                            antiga.getIdAlvara(),
                            novaQualif.getId(),
                            "A"
                    );

                    if (jaExiste) {
                        continue;
                    }

                    FormacaoEntity novaFormacao = new FormacaoEntity();
                    novaFormacao.setIdAlvara(antiga.getIdAlvara());
                    novaFormacao.setDmTipoFormacao(antiga.getDmTipoFormacao());

                    AreaQualifEntity areaPai = null;
                    if (novaQualif.getIdPai() != null) {
                        areaPai = findById(novaQualif.getIdPai());
                    }

                    novaFormacao.setIdQualificacao(novaQualif.getId());
                    novaFormacao.setQualificacaoDesc(novaQualif.getDescricao());
                    novaFormacao.setIdAreaQualif(novaQualif.getIdPai());
                    novaFormacao.setAreaDesc(areaPai != null ? areaPai.getDescricao() : null);

                    novaFormacao.setDmNivelRomano(novaDto.getNivel());
                    novaFormacao.setFlagCatalogo(antiga.getFlagCatalogo());
                    novaFormacao.setDtRegisto(LocalDate.now());
                    novaFormacao.setDmMetodologia(antiga.getDmMetodologia());
                    novaFormacao.setSelfIdCnq(novaQualif.getSelfIdCnq());
                    novaFormacao.setVersao(novaQualif.getVersao());

                    // campo de estado da acreditação
                    novaFormacao.setDmEstado("PENDENTE");

                    formacaoRepository.save(novaFormacao);
                    pendentesCriadas++;
                }
            }
        }

        return new RevogacaoResult(true, inativadas, pendentesCriadas);
    }
    public AreaQualifEntity findOrCreateQualification(AreaQualifDTO dto) {
        return repo.findBySiglaCodigoAndVersao(dto.getCodigoQualif(), dto.getVersao())
                .orElseGet(() -> {
                    AreaQualifEntity entity = new AreaQualifEntity();
                    entity.setSiglaCodigo(dto.getCodigoQualif());
                    entity.setVersao(dto.getVersao());
                    entity.setDescricao(dto.getDenominacaoQualif());
                    entity.setDmNivelArabico(dto.getNivel());
                    entity.setSelfIdCnq(dto.getSelfId() != null ? dto.getSelfId().toString() : null);
                    entity.setEstado("A");

                    if (dto.getCodigoFamilia() != null && !dto.getCodigoFamilia().isBlank()) {
                        AreaQualifEntity familia = findOrCreateFamily(dto.getCodigoFamilia(), dto.getDenominacaoFamilia());
                        entity.setIdPai(familia.getId());
                    }

                    return repo.save(entity);
                });
    }
    @Override
    public AreaQualifEntity findById(String id) {
        return repo.findById(id).orElse(null);
    }
}
