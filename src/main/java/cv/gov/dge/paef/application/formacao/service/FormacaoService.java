// src/main/java/cv/gov/dge/paef/domain/formacao/FormacaoService.java
package cv.gov.dge.paef.application.formacao.service;

import cv.gov.dge.paef.helpers.Utils;
import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.infrastructure.repository.FormacaoRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.Entidade.RvccEntidadeDTO;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoOutroDto;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoOutroFilter;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.RvccFormacaoRow;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.RvccQualificacaoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service @Transactional
public class FormacaoService {
    private final FormacaoRepository repo;

    public FormacaoService(FormacaoRepository repo) {
        this.repo = repo;
    }

    public FormacaoEntity save(FormacaoEntity e) {
        return repo.save(e);
    }

    public List<FormacaoOutroDto> listarFormacoesOutro(FormacaoOutroFilter filter) {
        String nrAlvara = Utils.nullToEmpty(filter.getNr_alvara());
        String entidadeNome = Utils.nullToEmpty(filter.getEntidade_nome());
        String familia = Utils.nullToEmpty(filter.getFamilia());
        String qualificacao = Utils.nullToEmpty(filter.getQualificacao());
        String nivel = Utils.nullToEmpty(filter.getNivel());
        return repo.findFormacoesOutro(nrAlvara, entidadeNome, familia, qualificacao, nivel)
                .stream()
                .map(f -> FormacaoOutroDto.builder()
                        .idAlvara(f.getIdAlvara())
                        .nrAlvara(f.getNrAlvara())
                        .entidadeNome(f.getEntidadeNome())
                        .familia(f.getFamilia())
                        .qualificacao(f.getQualificacao())
                        .nivel(f.getNivel())
                        .idFormacao(f.getIdFormacao())
                        .build())
                .toList();
    }

    public EnvelopeData<List<RvccEntidadeDTO>> listarQualificacoesRvcc() {

        List<RvccFormacaoRow> rows = repo.findRvccFormacoes();

        Map<String, Agrupador> agrupado = new LinkedHashMap<>();

        for (RvccFormacaoRow row : rows) {

            String chave = row.getNif() + "|" + row.getNumAlvara();

            Agrupador agg = agrupado.computeIfAbsent(chave, k -> new Agrupador(
                    row.getNif(),
                    row.getDesignacaoComercial(),
                    row.getIlhaNome(),
                    row.getConcelhoNome(),
                    row.getConcelhoId(),
                    row.getEndereco(),
                    row.getNumAlvara(),
                    row.getEstadoAlvara()
            ));

            RvccQualificacaoDTO qualificacao = RvccQualificacaoDTO.builder()
                    .codigoCnq(row.getCodigoCnq())
                    .selfidQp(row.getSelfidQp())
                    .denominacao(row.getDenominacao())
                    .familia(row.getFamilia())
                    .codigoFamilia(row.getCodigoFamilia())
                    .nivelQnq(row.getNivelQnq())
                    .idFormacao(row.getIdFormacao())
                    .build();

            boolean ativa = Boolean.TRUE.equals(row.getFlagRvcc()) && row.getSendedToRvcc() == null;
            boolean inativa = Boolean.TRUE.equals(row.getSendedToRvcc()) && !Boolean.TRUE.equals(row.getFlagRvcc());

            if (ativa) {
                agg.qualificacoesAtivas.add(qualificacao);
            } else if (inativa) {
                agg.qualificacoesInativas.add(qualificacao);
            }
        }

        List<RvccEntidadeDTO> data = agrupado.values().stream()
                .map(a -> RvccEntidadeDTO.builder()
                        .nif(a.nif)
                        .designacaoComercial(a.designacaoComercial)
                        .ilha(a.ilha)
                        .concelho(a.concelho)
                        .idConselho(a.idConselho)
                        .endereco(a.endereco)
                        .numAlvara(a.numAlvara)
                        .estadoAlvara(a.estadoAlvara)
                        .qualificacoesAtivas(a.qualificacoesAtivas)
                        .qualificacoesInativas(a.qualificacoesInativas)
                        .build())
                .toList();

        return new EnvelopeData<>(data);
    }

    private static class Agrupador {
        String nif;
        String designacaoComercial;
        String ilha;
        String concelho;
        String idConselho;
        String endereco;
        String numAlvara;
        String estadoAlvara;
        List<RvccQualificacaoDTO> qualificacoesAtivas = new ArrayList<>();
        List<RvccQualificacaoDTO> qualificacoesInativas = new ArrayList<>();

        Agrupador(String nif, String designacaoComercial, String ilha, String concelho,
                  String idConselho, String endereco, String numAlvara, String estadoAlvara) {
            this.nif = nif;
            this.designacaoComercial = designacaoComercial;
            this.ilha = ilha;
            this.concelho = concelho;
            this.idConselho = idConselho;
            this.endereco = endereco;
            this.numAlvara = numAlvara;
            this.estadoAlvara = estadoAlvara;
        }
    }

        @Transactional
        public ApiResponse<?> markSent(List<String> ids) {

            int updated = repo.markAsSentToRvcc(ids);

            return ApiResponse.ok(
                    "Registos marcados como enviados com sucesso", null
            );
        }
    }

