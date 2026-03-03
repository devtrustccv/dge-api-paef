package cv.gov.dge.paef.application.Pagamento.service;

import cv.gov.dge.paef.application.domain.service.DominioService;
import cv.gov.dge.paef.helpers.Utils;
import cv.gov.dge.paef.infrastructure.VPaefPagamentoEntity;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.PagamentoRepository;
import cv.gov.dge.paef.infrastructure.repository.VPagamentoRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.pagamento.PagamentoListResponse;
import cv.gov.dge.paef.interfaces.dto.pagamento.PagamentoRowDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PagamentoService {

    private final EntidadeRepository entidadeRepo; // findByNif
    private final VPagamentoRepository vPagRepo;
    private final PagamentoRepository pagRepo;
    private final DominioService dominioService;

    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PagamentoService(EntidadeRepository entidadeRepo,
                            VPagamentoRepository vPagRepo,
                            PagamentoRepository pagRepo,
                            DominioService dominioService) {
        this.entidadeRepo = entidadeRepo;
        this.vPagRepo = vPagRepo;
        this.pagRepo = pagRepo;
        this.dominioService = dominioService;
    }

    public ApiResponse<?> listar(BigDecimal nif, String idAlvara, String estado, String nrProcesso, Integer limit) {

        var ent = entidadeRepo.findByNif(nif).orElse(null);
        if (ent == null) {
            return  ApiResponse.fail( "Entidade não encontrada para o NIF informado", null);
        }

        int size = limit == null ? 100 : Math.min(Math.max(limit, 1), 200);

        List<VPaefPagamentoEntity> rows = vPagRepo.findPagamentos(
                ent.getId(),
                Utils.blank(idAlvara) ? null : idAlvara,
                Utils.blank(estado) ? null : estado, 
                Utils.blank(nrProcesso) ? null : nrProcesso
        );

        var data = rows.stream().limit(size).map(p -> {
            boolean pago = "PAGO".equalsIgnoreCase(p.getDmEstadoPag());
            boolean hasDuc = !Utils.blank(p.getDuc());
            boolean hasRecibo = !Utils.blank(p.getDocClob());
            boolean atrasado = "ATRASADO".equalsIgnoreCase(p.getEstadoPagamento());

            return PagamentoRowDTO.builder()
                    .idPag(p.getIdPagamento())
                    .alvara(p.getNrAlvara())
                    .dataRegisto(p.getDataRegisto() == null ? null : D.format(p.getDataRegisto()))
                    .dtPgto(p.getDataPag() == null ? null : D.format(p.getDataPag()))
                    .dtPrevpagto(p.getDataPrevPag() == null ? null : D.format(p.getDataPrevPag()))
                    .estado(p.getDmEstadoPag())

                    // botões (inversão do legacy hiddenButton)
                    .showPagar(!pago && hasDuc)
                    .showConfirmarPagamento(!pago && hasDuc)
                    .showReciboPagamento(pago && hasRecibo)
                    .showVerDuc(hasDuc)

                    .docClob(p.getDocClob())
                    .isAtrasado(atrasado)

                    .entidadeV(p.getEntidade())
                    .referencia(p.getReferencia() == null ? "" : p.getReferencia().toPlainString())
                    .valorV(p.getValor() == null ? "" : p.getValor().toPlainString())
                    .processo(p.getIdProcesso())
                    .tipo(dominioService.getDesc("TIPO_PAGAMENTO", p.getFormPagamento()).description())
                    .valor(formatCVE(p.getValor()))
                    .duc(p.getDuc())
                    .parcela(Utils.blank(p.getNrParcela()) ? "1" : p.getNrParcela())
                    .build();
        }).toList();

        BigDecimal totalPago = pagRepo.sumByEstado("PAGO", ent.getId());
        BigDecimal totalPendente = pagRepo.sumByEstado("PENDENTE", ent.getId());
        BigDecimal dividaAtraso = pagRepo.dividaAtraso(ent.getId());

        var payload = new PagamentoListResponse(
                data,
                formatCVE(totalPago),
                formatCVE(totalPendente),
                formatCVE(dividaAtraso)
        );

        return ApiResponse.ok( "Lista Pagamento Carregado com sucesso", payload);
    }

    private static String formatCVE(BigDecimal v) {
        if (v == null) v = BigDecimal.ZERO;
        // replica teu estilo: "1.234 CVE" (sem casas decimais)
        DecimalFormat df = new DecimalFormat("#,##0");
        String out = df.format(v).replace(",", ".");
        return out + " CVE";
    }
}
