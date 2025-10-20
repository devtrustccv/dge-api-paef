package cv.gov.dge.paef.application.entidade.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record EntidadePffpDTO(

        String denominacao,
        @JsonProperty("denominacao_norm") String denominacaoNorm,
        Long nif,
        String natureza,
        String email,
        String url,
        @JsonProperty("geog_local_id") String geogLocalId,
        String morada,
        String telemovel,
        String telefone,
        @JsonProperty("nr_alvara") String nrAlvara,
        @JsonProperty("nome_ponto_focal") String nomePontoFocal,
        @JsonProperty("contas_acesso") List<ContaAcessoDTO> contasAcesso
) {
    @Builder
    public record ContaAcessoDTO(
            @JsonProperty("email_user") String emailUser,
            @JsonProperty("flag_master") String flagMaster
    ) {}
}
