package cv.gov.dge.paef.interfaces.dto.pagamento;

import lombok.Builder;

@Builder
public record ConfirmarPagamentoResponseDTO(
        String idPag,
        String duc,
        String estadoPagamento,
        String dataPag
) {}
