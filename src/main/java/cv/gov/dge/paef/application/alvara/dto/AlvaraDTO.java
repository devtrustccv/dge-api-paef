package cv.gov.dge.paef.application.alvara.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AlvaraDTO(
        String id,
        @NotBlank String idEntidade,
        String idPedido,
        @NotBlank String idTipoAlvara,
        String idProcesso,
        @NotBlank String dmEstadoAlvara,
        LocalDate dataPedido,
        @NotNull LocalDate dataEmissao,
        @NotNull LocalDate dataValidade,
        String idEstabelecimento,
        String nrProcesso,
        String dmTipoValidade,
        LocalDate dataDespacho,
        String observacao,
        String dmSituacao,
        String parceria,
        String nrAlvara,
        String dmOrigemReg,
        LocalDate dtEnvIncv,
        String referenciaBo,
        LocalDate dtPubBo,
        String obsBo,
        String linkReportAlvara,
        String flagPublicacao,
        String contraProva
) {}
