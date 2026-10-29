package cv.gov.dge.paef.interfaces.dto.notificacao;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record NotificacaoRespostaRequestDTO(
        @NotNull @Positive BigDecimal nif,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 4000) String mensagem
) {}
