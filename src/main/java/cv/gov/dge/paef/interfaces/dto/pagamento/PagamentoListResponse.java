package cv.gov.dge.paef.interfaces.dto.pagamento;

import java.util.List;

public record PagamentoListResponse(
        List<PagamentoRowDTO> data,
        String totalPago,
        String totalPendente,
        String dividaAtraso
) {}
