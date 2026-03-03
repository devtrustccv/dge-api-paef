package cv.gov.dge.paef.application.Pagamento.service;

import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.PagamentoRepository;
import cv.gov.dge.paef.infrastructure.repository.PedidoRepository;
import cv.gov.dge.paef.integration.DucClient;
import cv.gov.dge.paef.integration.dto.Duc;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.pagamento.ConfirmarPagamentoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PagamentoConfirmacaoService {

    private final EntidadeRepository entidadeRepo;
    private final PagamentoRepository pagamentoRepo;
    private final PedidoRepository pedidoRepo;
    private final DucClient ducClient;
   // private final WorkflowService workflowService; // wrapper do teu motor (Activiti/Flowable/etc)

    public PagamentoConfirmacaoService(
            EntidadeRepository entidadeRepo,
            PagamentoRepository pagamentoRepo,
            PedidoRepository pedidoRepo,
            DucClient ducClient//,
           // WorkflowService workflowService
    ) {
        this.entidadeRepo = entidadeRepo;
        this.pagamentoRepo = pagamentoRepo;
        this.pedidoRepo = pedidoRepo;
        this.ducClient = ducClient;
       //this.workflowService = workflowService;
    }

    @Transactional
    public ApiResponse<?> confirmar(BigDecimal nif, String nDuc) {

        var ent = entidadeRepo.findByNif(nif).orElse(null);
        if (ent == null) {
            return ApiResponse.fail( "Entidade não encontrada para o NIF informado", null);
        }

        var pag = pagamentoRepo.findByDuc(nDuc).orElse(null);
        if (pag == null) {
            return ApiResponse.fail( "Pagamento não encontrado para esta entidade", null);
        }


        Duc duc;
        try {
            duc = ducClient.consultarEstadoDuc(pag.getDuc(), "");
        } catch (Exception ex) {
            return ApiResponse.fail( "Erro ao consultar DUC: " + ex.getMessage(), null);
        }

        // aqui no legado: compara estadoDescricao com "PAGO"
        if (!"PAGO".equalsIgnoreCase(duc.getEstadoDescricao())) {
            String parcela = (pag.getNrParcela() == null || pag.getNrParcela().isBlank()) ? "1" : pag.getNrParcela();
            return ApiResponse.fail(
                    "Duc referente a " + parcela + "ª parcela não se encontra pago. Se fez agora o pagamento poderá ter de esperar 24h para a confirmação!",
                    null
            );
        }

        // idempotência
        if ("PAGO".equalsIgnoreCase(pag.getDmEstadoPag())) {
            return  ApiResponse.ok( "Pagamento já confirmado", ConfirmarPagamentoResponseDTO.builder()
                    .idPag(pag.getId())
                    .duc(pag.getDuc())
                    .estadoPagamento(pag.getDmEstadoPag())
                    .dataPag(pag.getDataPag() == null ? null : pag.getDataPag().toString())
                    .build());
        }

        // Atualiza pagamento
        pag.setDataPag(LocalDate.now());
        pag.setDmEstadoPag("PAGO"); // usa constante getConfig.PAGO equivalente
        pagamentoRepo.save(pag);

        // Atualiza pedido (se existir)
        if (pag.getIdProcesso() != null && !pag.getIdProcesso().isBlank()) {
            pedidoRepo.findByIdProcesso(BigDecimal.valueOf(Integer.parseInt(pag.getIdProcesso()))).ifPresent(ped -> {
                ped.setValorPago(pag.getValor());
                ped.setDtPagamento(LocalDate.now());

                // avançar workflow apenas se etapa atual == PAGAMENTO
               /*TODO if ("PAGAMENTO".equalsIgnoreCase(ped.getEtapaAtual())) {
                    workflowService.registarPagamento(ped.getIdProcesso(), pag); // encapsula o “assignTask + executeTask”
                    // depois de avançar, atualiza etapaAtual (buscando task atual)
                    String etapaAtual = workflowService.getEtapaAtual(ped.getIdProcesso());
                    ped.setEtapaAtual(etapaAtual);
                }*/

                pedidoRepo.save(ped);
            });
        }

        return ApiResponse.ok( "Pagamento confirmado com sucesso", ConfirmarPagamentoResponseDTO.builder()
                .idPag(pag.getId())
                .duc(pag.getDuc())
                .estadoPagamento(pag.getDmEstadoPag())
                .dataPag(pag.getDataPag() == null ? null : pag.getDataPag().toString())
                .build());

}}
