package cv.gov.dge.paef.application.entidade.service;

import cv.gov.dge.paef.application.entidade.dto.EntidadeDetalheDTO;
import cv.gov.dge.paef.application.entidade.dto.EntidadePffpDTO;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeAcreditacaoRow;
import cv.gov.dge.paef.interfaces.dto.PffpMarkSentResultDTO;
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
                                .denominacaoFamilia(r.getDenominacaoFamilia())
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

    public List<EntidadePffpDTO> listEntidadesPendentesPFFP() {
        var rows = entidadeRepository.findEntidadesPendentesPffp();

        Map<String, EntidadePffpDTO.EntidadePffpDTOBuilder> builders = new LinkedHashMap<>();
        Map<String, List<EntidadePffpDTO.ContaAcessoDTO>> contasByEnt = new LinkedHashMap<>();

        for (var r : rows) {
            builders.computeIfAbsent(r.getEntId(), id ->
                    EntidadePffpDTO.builder()
                            .denominacao(r.getDenominacao())
                            .denominacaoNorm(r.getDenominacaoNorm())
                            .nif(r.getNif() == null ? null : r.getNif().longValue())
                            .natureza(r.getNatureza())
                            .email(r.getEmail())
                            .url(r.getUrl())
                            .geogLocalId(r.getGeogLocalId())
                            .morada(r.getMorada())
                            .telemovel(r.getTelemovel())
                            .telefone(r.getTelefone())
                            .nrAlvara(r.getNrAlvara())
                            .nomePontoFocal(r.getNomePontoFocal())
                            .contasAcesso(new ArrayList<>())
            );
            contasByEnt.computeIfAbsent(r.getEntId(), id -> new ArrayList<>());
            if (r.getEmailUser() != null) {
                contasByEnt.get(r.getEntId()).add(
                        EntidadePffpDTO.ContaAcessoDTO.builder()
                                .emailUser(r.getEmailUser())
                                .flagMaster(r.getFlagMaster())
                                .build()
                );
            }
        }

        List<EntidadePffpDTO> result = new ArrayList<>();
        builders.forEach((id, b) -> {
            b.contasAcesso(contasByEnt.getOrDefault(id, List.of()));
            result.add(b.build());
        });
        return result;

    }

    @Transactional
    public List<PffpMarkSentResultDTO> markManyByNifs(List<Long> nifs) {
        List<PffpMarkSentResultDTO> results = new ArrayList<>();
        for (Long nif : nifs) {
            int updated = entidadeRepository.markPffpSentByNif(new BigDecimal(nif));
            results.add(PffpMarkSentResultDTO.builder()
                    .nif(nif)
                    .updated(updated > 0)
                    .message(updated > 0 ? "Marcado sended_to_pffp=true" : "NIF não encontrado")
                    .build());
        }
        return results;
    }
}
