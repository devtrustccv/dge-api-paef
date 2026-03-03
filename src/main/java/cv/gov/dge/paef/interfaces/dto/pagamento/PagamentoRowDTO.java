package cv.gov.dge.paef.interfaces.dto.pagamento;

import lombok.Builder;

@Builder
public record PagamentoRowDTO(
        String idPag,
        String alvara,
        String dataRegisto,
        String dtPgto,
        String dtPrevpagto,
        String estado,
        Boolean showPagar,
        Boolean showConfirmarPagamento,
        Boolean showReciboPagamento,
        Boolean showVerDuc,
        String docClob,
        Boolean isAtrasado,
        String entidadeV,
        String referencia,
        String valorV,
        String processo,
        String tipo,
        String valor,
        String duc,
        String parcela,
        String linkVerDuc,
        String linkPagar
) {}
