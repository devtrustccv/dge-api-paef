package cv.gov.dge.paef.interfaces.dto.Alvara;

import jakarta.validation.constraints.Email;

public record EstabelecimentoUpdateRequest(
        String zonaId,
        String telefone,
        String telemovel,
        String endereco,
        String caixaPostal,

        String nomeResp,
        String tipoDocumentoResp,
        String nrDocumentoResp,
        String telefoneResp,
        String telemovelResp,
        @Email(message="Email do responsável inválido") String emailResp,
        String enderecoResp,
        String nivelFormacaoAcademica,

        String actorEmail
) {}