package cv.gov.dge.paef.application.pedido.service;

import cv.gov.dge.paef.application.domain.service.DominioService;
import cv.gov.dge.paef.helpers.PeriodoParser;
import cv.gov.dge.paef.infrastructure.TPedidoEntity;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.PedidoRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.Pedido.PedidoListFilter;
import cv.gov.dge.paef.interfaces.dto.Pedido.PedidoListItemDTO;
import cv.gov.dge.paef.interfaces.dto.Pedido.PedidoSpecs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PedidoService {

    private static final String DEFAULT_ESTADO = "PENDENTE";
    private static final String BACKEND = "BACKEND";
    private static final String ETAPA_FEEDBACK = "FEEDBACK";

    private final PedidoRepository repo;
    private final EntidadeRepository entidadeRepository;
    private final DominioService dominioService;
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Value("${link.page.details_process}") String linkDetalhe;

    public PedidoService(PedidoRepository repo,
                         EntidadeRepository entidadeRepository,
                         DominioService dominioService
    ) {
        this.repo = repo;
        this.entidadeRepository = entidadeRepository;
        this.dominioService = dominioService;
    }

    public ApiResponse<?> listar(BigDecimal nifEntidade, PedidoListFilter filter) {

        var range = PeriodoParser.parse(filter.periodo());
        var start = range[0];
        var end = range[1];

        var ent = entidadeRepository.findByNif(nifEntidade).orElse(null);
        if (ent == null) {
            return ApiResponse.fail( "Entidade não encontrada para o NIF informado", null);
        }

        String idEntidade = ent.getId();

        var spec = Specification
                .where(PedidoSpecs.byIdEntidade(idEntidade))
                .and(PedidoSpecs.estadoEquals(filter.estado(), DEFAULT_ESTADO))
                .and(PedidoSpecs.tipoPedidoEquals(filter.tipoPedido()))
                .and(PedidoSpecs.excludeFiscalizacao())
                .and(PedidoSpecs.dtRegistoBetween(start, end));

        int page = filter.page() == null ? 0 : Math.max(filter.page(), 0);
        int size = filter.size() == null ? 50 : Math.min(Math.max(filter.size(), 1), 200);

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dtRegisto"));
        Page<TPedidoEntity> rows = repo.findAll(spec, pageable);

        List<PedidoListItemDTO> data = rows.getContent().stream().map(p -> {

            String user= p.getNomeEntrega();


          /*  String acao = "";
            String acaoDesc = "";
            if (p.getEtapaAtual() != null && ETAPA_FEEDBACK.equalsIgnoreCase(p.getEtapaAtual())) {
                acao = hostnameService.baseUrl() + "/paef/registo-de-feedback"
                        + "?p_id_processo=" + p.getIdProcesso()
                        + "&p_id_etapa=" + p.getIdEtapa();
                acaoDesc = "Dar feedBack";
            }*/

            return PedidoListItemDTO.builder()
                    .nProcesso(p.getIdProcesso().toString())
                    .tipoPedidoLis(p.getTpPedido())
                    .utilizadorRegisto(user)
                    .dataPedido(p.getDtRegisto() == null ? null : OUT.format(p.getDtRegisto()))
                    .dataFim(p.getDtFim() == null ? null : OUT.format(p.getDtFim()))
                    .estadoEtapa(dominioService.getDesc("ESTADO_PROC", p.getDmEstadoPedido()).description())
                    .resultado(dominioService.getDesc("DECISAO", p.getResultado()).description())
                    .idProcesso(p.getIdProcesso().toString())
                    .idTask(p.getIdEtapa())
                    .etapaAtual(p.getEtapaAtual())
                    .idPedido(p.getId())
                    .linkDetalhe(linkDetalhe+p.getIdEtapa())
                    .build();
        }).toList();

        return  ApiResponse.ok( "Lista Caregada com sucesso", data);
    }
}
