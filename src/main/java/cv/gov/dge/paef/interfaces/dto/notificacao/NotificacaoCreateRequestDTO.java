package cv.gov.dge.paef.interfaces.dto.notificacao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NotificacaoCreateRequestDTO(
        @NotNull @Positive BigDecimal nif,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 255) String assunto,
        @NotBlank @Size(max = 4000) String mensagem,
        String idAlvara,
        String idProcesso,
        String idPaginaEtapa
) {}
