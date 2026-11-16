package cv.gov.dge.paef.application.AreaQualif.service;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.application.AreaQualif.dto.RevogacaoResult;
import cv.gov.dge.paef.domain.areaqualif.business.AreaQualifBus;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.domain.areaqualif.model.DuplicateQualificationException;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.infrastructure.mapper.AreaQualifMapper;
import cv.gov.dge.paef.infrastructure.repository.AreaQualifRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AreaQualifServiceImpl implements AreaQualifService {

    private final AreaQualifBus bus;
    private final AreaQualifMapper mapper;
    private final AreaQualifRepository repo;

    /**
     * Cria/atualiza registro da paef.paef_t_area_qualif.
     * Regras:
     * - estado default 'A'
     * - id = codigoQualif
     */
    @Transactional
    public AreaQualif createOrUpdate(AreaQualifDTO dto) {

        String familiaCod = dto.getCodigoFamilia();
        String familiaDesc = dto.getDenominacaoFamilia();
        String familiaId = null;

        if (familiaCod != null && !familiaCod.isBlank()) {
            AreaQualifEntity familia = bus.findOrCreateFamily(familiaCod, familiaDesc);
            familiaId = familia.getId();
        }

        // procurar existente
        AreaQualifEntity entity = bus.findBySiglaCodigoAndVersao(dto.getCodigoQualif(), dto.getVersao())
                .orElseGet(AreaQualifEntity::new);

        // mapear/update dos campos
        entity.setSiglaCodigo(dto.getCodigoQualif());
        entity.setVersao(dto.getVersao());
        entity.setDescricao(dto.getDenominacaoQualif());
        entity.setDmNivelArabico(dto.getNivel());
        entity.setSelfIdCnq(dto.getSelfId() != null ? dto.getSelfId().toString() : null);

        if (entity.getEstado() == null || entity.getEstado().isBlank()) {
            entity.setEstado("A");
        }

        if (familiaId != null) {
            entity.setIdPai(familiaId);
        }

        AreaQualifEntity saved = bus.save(entity);

        return mapper.toModel(saved);
    }

    public boolean findExisting(String codigo, String versao){
        return bus.existsQualification(codigo, versao);
    }

    @Override
    @Transactional
    public AreaQualif revoke(AreaQualifDTO dto) {
        return mapper.toModel(bus.revoke(dto));
    }

    @Override
    @Transactional
    public Map<String, Integer> processQualificacoes(List<AreaQualifDTO> dtos) {

        int criados = 0;
        int atualizados = 0;
        int revogados = 0;
        int rvcc = 0;
        int ignorados = 0;
        int acreditacoesInativadas = 0;
        int acreditacoesPendentesCriadas = 0;

        for (AreaQualifDTO dto : dtos) {

            if (dto.getCodigoQualif() == null || dto.getVersao() == null) {
                ignorados++;
                continue;
            }

            boolean existing = bus.findExisting(dto.getCodigoQualif(), dto.getVersao());

            if ("PUBLICADO".equals(dto.getTipoIntegraca())) {
                createOrUpdate(dto);
                if (existing) atualizados++;
                else criados++;

            } else if ("REVOGADO".equals(dto.getTipoIntegraca())) {

                RevogacaoResult result = bus.revokeAndReplace(dto);

                if (result.qualificacaoRevogada()) {
                    revogados++;
                } else {
                    ignorados++;
                }

                acreditacoesInativadas += result.acreditacoesInativadas();
                acreditacoesPendentesCriadas += result.acreditacoesPendentesCriadas();

            } else if ("RVCC".equals(dto.getTipoIntegraca())) {
                bus.markAsRvcc(dto);
                rvcc++;

            } else {
                ignorados++;
            }
        }

        return Map.of(
                "criados", criados,
                "atualizados", atualizados,
                "revogados", revogados,
                "rvcc", rvcc,
                "ignorados", ignorados,
                "acreditacoesInativadas", acreditacoesInativadas,
                "acreditacoesPendentesCriadas", acreditacoesPendentesCriadas,
                "total", dtos.size()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDTO> listQualificacoesByFamilia(String idFamilia) {
        List<OptionDTO> options = repo.findQualificacoesByFamilia(idFamilia).stream()
                .map(r -> OptionDTO.builder()
                        .key(r.getKey())
                        .value(r.getValue())
                        .build())
                .toList();

        List<OptionDTO> result = new java.util.ArrayList<>();
        result.add(OptionDTO.builder()
                .key("OUTRO")
                .value("Outro")
                .build());
        result.addAll(options);

        return result;
    }

}
