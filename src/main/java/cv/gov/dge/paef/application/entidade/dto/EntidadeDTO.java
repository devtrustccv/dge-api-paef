// src/main/java/cv/gov/dge/paef/interfaces/dto/EntidadeDTO.java
package cv.gov.dge.paef.application.entidade.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EntidadeDTO(
        String id,
        @NotBlank String denominacaoSocial,
        String denominacaoComercial,
        @NotNull Long nif,
        String dmNatureza,
        LocalDate dtConstituicao,
        String nrMatricula,
        String endereco,
        String caixaPostal,
        String telefone,
        String telemovel,
        @Email String email,
        String url,
        String representanteLegal,
        @NotBlank String dmOrigemReg,
        @NotNull LocalDate dataRegisto,
        @NotNull Long userRegistoId,
        String abreviatura,
        String geogLocalId,
        String denominacaoSocialNorm,
        Boolean isOnPortal
) {}
