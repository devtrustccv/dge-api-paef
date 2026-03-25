package cv.gov.dge.paef.web.pagamento;

import cv.gov.dge.paef.application.Pagamento.service.PagamentoConfirmacaoService;
import cv.gov.dge.paef.application.Pagamento.service.PagamentoService;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService service;
    private final PagamentoConfirmacaoService pcService;

    public PagamentoController(PagamentoService service, PagamentoConfirmacaoService pcService) {
        this.service = service;
        this.pcService=pcService;
    }

    @GetMapping
    public ResponseEntity<EnvelopeData<?>> listar(
            @RequestParam("nif") @NotNull @Positive BigDecimal nif,
            @RequestParam(value="alvara", required=false) String alvara,
            @RequestParam(value="estado_pagamento", required=false) String estadoPagamento,
            @RequestParam(value="nr_processo", required=false) String nrProcesso,
            @RequestParam(value="limit", required=false) Integer limit
    ) {
        return ResponseEntity.ok(new EnvelopeData<>(service.listar(nif, alvara, estadoPagamento, nrProcesso, limit)));
    }

    @PostMapping("/confirmar")
    public ResponseEntity<EnvelopeData<?>> confirmar( @RequestParam("nif") @NotNull @Positive BigDecimal nif,
                                                      @RequestParam(value="duc") @NotNull @Positive String duc) {
        return ResponseEntity.ok(new EnvelopeData<>(pcService.confirmar(nif, duc)));
    }
}
