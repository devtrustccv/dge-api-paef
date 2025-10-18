package cv.gov.dge.paef.application.entidade.service;

import cv.gov.dge.paef.application.entidade.dto.EntidadeDetalheDTO;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.interfaces.dto.EntidadeAcreditacaoRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EntidadeQueryService {

    private final EntidadeRepository entidadeRepository;

    public EntidadeDetalheDTO getDetalheByNif(BigDecimal nif) {
        var ent = entidadeRepository.findByNif(nif).orElse(null);
        var acreditacoesProj = entidadeRepository.findAcreditacoesByNif(nif);

        var acreditacoes = acreditacoesProj.stream()
                .map(p -> EntidadeDetalheDTO.AcreditacaoDTO.builder()
                        .codigoFamilia(p.getCodigoFamilia())
                        .nivel(p.getNivel())
                        .self(p.getSelf())
                        .versao(p.getVersao())
                        .codigoQualificacao(p.getCodigoQualificacao())
                        .denominacaoQualificacao(p.getDenominacaoQualificacao())
                        .modalidade(p.getModalidade())
                        .metodologia(p.getMetodologia())
                        .nrAlvara(p.getNrAlvara())
                        .idAlvara(p.getIdAlvara())
                        .statusAlvara(p.getStatusAlvara())
                        .flagCatalogo(p.getFlagCatalogo())
                        .situacao(p.getSituacao())
                        .build())
                .toList();

        if (ent == null) {
            return EntidadeDetalheDTO.builder()
                    .denominacao(null)
                    .nif(nif)
                    .natureza(null)
                    .email(null)
                    .url(null)
                    .geogLocalId(null)
                    .acreditacoes(acreditacoes)
                    .build();
        }

        return EntidadeDetalheDTO.builder()
                .denominacao(ent.getDenominacaoSocial())
                .nif(ent.getNif())
                .natureza(ent.getDmNatureza())
                .email(ent.getEmail())
                .url(ent.getUrl())
                .geogLocalId(ent.getGeogLocalId())
                .acreditacoes(acreditacoes)
                .build();
    }

    public List<EntidadeDetalheDTO> listEntidadesPendentesSgf() {
        List<EntidadeAcreditacaoRow> rows = entidadeRepository.findEntidadesComAlvaraNaoEnviadoSgf();

        // Agrupar por entidade
        Map<String, EntidadeDetalheDTO.EntidadeDetalheDTOBuilder> builders = new LinkedHashMap<>();
        Map<String, List<EntidadeDetalheDTO.AcreditacaoDTO>> accByEnt = new LinkedHashMap<>();

        for (EntidadeAcreditacaoRow r : rows) {
            builders.computeIfAbsent(r.getEntId(), id -> EntidadeDetalheDTO.builder()
                    .denominacao(r.getDenominacao())
                    .nif(r.getNif() == null ? null : r.getNif())
                    .natureza(r.getNatureza())
                    .email(r.getEmail())
                    .url(r.getUrl())
                    .geogLocalId(r.getGeogLocalId())
                    .acreditacoes(new ArrayList<>())
            );
            accByEnt.computeIfAbsent(r.getEntId(), id -> new ArrayList<>());

            // Se a linha tem formação/alvará, adiciona acreditação; senão, deixa lista vazia
            if (r.getCodigoQualificacao() != null || r.getIdAlvara() != null) {
                accByEnt.get(r.getEntId()).add(
                        EntidadeDetalheDTO.AcreditacaoDTO.builder()
                                .codigoFamilia(r.getCodigoFamilia())
                                .nivel(r.getNivel())
                                .self(r.getSelf())
                                .versao(r.getVersao())
                                .codigoQualificacao(r.getCodigoQualificacao())
                                .denominacaoQualificacao(r.getDenominacaoQualificacao())
                                .modalidade(r.getModalidade())
                                .metodologia(r.getMetodologia())
                                .nrAlvara(r.getNrAlvara())
                                .idAlvara(r.getIdAlvara())
                                .statusAlvara(r.getStatusAlvara())
                                .flagCatalogo(r.getFlagCatalogo())
                                .situacao(r.getSituacao())
                                .build()
                );
            }
        }

        // Construir lista final mantendo a ordem de chegada
        List<EntidadeDetalheDTO> result = new ArrayList<>();
        builders.forEach((id, b) -> {
            List<EntidadeDetalheDTO.AcreditacaoDTO> list = accByEnt.getOrDefault(id, List.of());
            b.acreditacoes(list);
            result.add(b.build());
        });
        return result;
    }
}
