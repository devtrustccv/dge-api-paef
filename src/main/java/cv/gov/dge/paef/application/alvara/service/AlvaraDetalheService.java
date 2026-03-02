package cv.gov.dge.paef.application.alvara.service;

import cv.gov.dge.paef.application.domain.service.DominioService;
import cv.gov.dge.paef.infrastructure.repository.*;
import cv.gov.dge.paef.interfaces.dto.Alvara.AlvaraDetalheDTO;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.EquipamentoDTO;
import cv.gov.dge.paef.interfaces.dto.EquipamentoRow;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoDTO;
import cv.gov.dge.paef.interfaces.dto.logs.HistoricoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AlvaraDetalheService {

    private final AlvaraRepository alvaraRepo;
    private final GeografiaRepository geoRepo;
    private final FormacaoRepository formRepo;
    private final EquipamentoRepository equipRepo;
    private final RhRepository rhRepo;
    private final LogAlteracaoRepository logRepo;
    private final DominioService dominioService; // teu serviço de domínios

    // Ajusta para teu config
    private static final String OUTRO = "OUTRO";
    private static final String CONTINUA = "CONTINUA";

    public AlvaraDetalheService(
            AlvaraRepository alvaraRepo,
            GeografiaRepository geoRepo,
            FormacaoRepository formRepo,
            EquipamentoRepository equipRepo,
            RhRepository rhRepo,
            LogAlteracaoRepository logRepo,
            DominioService dominioService
    ) {
        this.alvaraRepo = alvaraRepo;
        this.geoRepo = geoRepo;
        this.formRepo = formRepo;
        this.equipRepo = equipRepo;
        this.rhRepo = rhRepo;
        this.logRepo = logRepo;
        this.dominioService = dominioService;
    }

    public ApiResponse<?> detalhes(String idAlvara) {

        var headerOpt = alvaraRepo.findHeader(idAlvara);
        if (headerOpt.isEmpty()) {
            return ApiResponse.fail( "Alvará não encontrado", null);
        }

        var h = headerOpt.get();
        var sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Situação (desc via domínios)
        String situacaoDesc = dominioService.getDesc("SITUACAO", h.getDmSituacao()).description();

        // Geografia do estabelecimento (pode ser null)
        var geo = (h.getLocalEstabelecimento() == null)
                ? null
                : geoRepo.findGeoById(h.getLocalEstabelecimento()).orElse(null);

        // Formações
        List<FormacaoDTO> formacoes = formRepo.findByAlvara(idAlvara).stream().map(f -> {

            String nivel;

            if (CONTINUA.equals(f.getDmTipoFormacao())) {
                nivel = "Sem Nivel";

            } else if (f.getIdQualificacao() != null
                    && !OUTRO.equalsIgnoreCase(f.getIdQualificacao())) {

                nivel = dominioService
                        .getDesc("NIVEL_ARABICO", f.getDmNivelArabico())
                        .description();

            } else {

                nivel = dominioService
                        .getDesc("NIVEL_ROMANO", f.getDmNivelRomano())
                        .description();
            }

            String tipo = dominioService.getDesc("TIPO_FORMACAO", f.getDmTipoFormacao()).description();
            String metodologia = dominioService.getDesc("METODOLOGIA", f.getDmMetodologia()).description();

            return FormacaoDTO.builder()
                    .familia(f.getFamilia())
                    .nivel(nivel)
                    .qualificacaoFormacao(f.getQualificacao())
                    .tipo(tipo)
                    .metodologia(metodologia)
                    .build();
        }).toList();

        // Equipamentos
        List<EquipamentoDTO> equipamentos = equipRepo.findByAlvara(idAlvara).stream()
                .map(e -> EquipamentoDTO.builder().descricao(e.getDescricao()).build())
                .toList();

        // RH
        List<OptionDTO> rhs = rhRepo.findByAlvara(idAlvara).stream()
                .map(r -> OptionDTO.builder()
                        .key(dominioService.getDesc("FUNCAO_RH", r.key()).description())
                        .value(r.value())
                        .build())
                .toList();

        // Histórico (username via serviço teu; aqui deixo “id” se não existir)
        List<HistoricoDTO> hist = logRepo.findHistoricoAlvara(idAlvara).stream()
                .map(l -> HistoricoDTO.builder()
                        .dataUser((l.getDataRegisto() == null ? "" : sdf.format(l.getDataRegisto()))
                                + "/" + (l.getUserRegistoId() == null ? "" : l.getUserRegistoId().toString()))
                        .estado(l.getCampoAlt())
                        .valorAnterior(l.getValorAnterior())
                        .valorAtual(l.getValorAtual())
                        .build())
                .toList();

        var dto = AlvaraDetalheDTO.builder()
                .idAlvara(h.getIdAlvara())
                .idEntidade(h.getIdEntidade())
                .idTask(h.getIdProcesso())
                .nrAlvara(h.getNrAlvara())
                .tipoAlvara(h.getDescricaoTipoAlvara())
                .situacao(situacaoDesc)
                .dataEmissao(h.getDataEmissao() == null ? null : sdf.format(h.getDataEmissao()))
                .dataPedido(h.getDataPedido() == null ? null : sdf.format(h.getDataPedido()))
                .dataValidade(h.getDataValidade() == null ? null : sdf.format(h.getDataValidade()))
                .estado(h.getEstadoAlvaraDescricao())
                .tipoProcesso(h.getDescricaoTipoAlvara())
                .nrProcesso(h.getNrProcesso())

                .designacao(h.getDesignacao())
                .ilhaId(geo == null ? "" : geo.getIlhaId())
                .ilha(geo == null ? "" : geo.getIlha())
                .concelho(geo == null ? "" : geo.getConcelho())
                .freguesia(geo == null ? "" : geo.getFreguesia())
                .zona(geo == null ? "" : geo.getZona())
                .concelhoId(geo == null ? "" : geo.getConcelhoId())
                .freguesiaId(geo == null ? "" : geo.getFreguesiaId())
                .zonaId(geo == null ? "" : geo.getZonaId())
                .endereco(h.getEnderecoEstabelecimento())
                .caixaPostal(h.getCaixaPostalEstabelecimento())
                .email(h.getEmailEstabelecimento())
                .telefone(h.getTelefoneEstabelecimento())
                .telemovel(h.getTelemovelEstabelecimento())

                .nomeResp(h.getNomeRespoinsavel())
                .tipoDocumentoResp(h.getDmTpDoc())
                .nrDocumentoResp(h.getNrDocResponsavel())
                .telemovelResp(h.getTelemovelResponsavel())
                .telefoneResp(h.getTelefoneResponsavel())
                .emailResp(h.getEmailResponsavel())
                .enderecoResp(null) // na view não vi endereco_resp; se existir na tabela, adicionamos
                .nivelFormacaoAcademica(h.getDmNivelAcademico())

                .formacoes(formacoes)
                .equipamentos(equipamentos)
                .recursosHumanos(rhs)
                .historico(hist)
                .build();

        return ApiResponse.ok("Detalhe Alvará carregado com sucesso",dto);
    }
}