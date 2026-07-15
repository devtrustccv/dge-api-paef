package cv.gov.dge.paef.interfaces.dto.Entidade;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record EntidadeCreateRequestDTO(
        @JsonProperty("denominacao_social")
        @NotBlank
        String denominacaoSocial,

        @JsonProperty("denominacao_comercial")
        String denominacaoComercial,

        @JsonProperty("nif")
        @NotNull
        @Positive
        Long nif,

        @JsonProperty("dm_natureza")
        String dmNatureza,

        @JsonProperty("dt_constituicao")
        LocalDate dtConstituicao,

        @JsonProperty("nr_matricula")
        String nrMatricula,

        @JsonProperty("endereco")
        String endereco,

        @JsonProperty("caixa_postal")
        String caixaPostal,

        @JsonProperty("telefone")
        String telefone,

        @JsonProperty("telemovel")
        String telemovel,

        @JsonProperty("email")
        @Email
        String email,

        @JsonProperty("url")
        String url,

        @JsonProperty("representante_legal")
        String representanteLegal,

        @JsonProperty("abreviatura")
        String abreviatura,

        @JsonProperty("geog_local_id")
        String geogLocalId,

        @JsonProperty("denominacao_social_norm")
        String denominacaoSocialNorm
) {}
