package cv.gov.dge.paef.interfaces.dto.ContaAcesso;

// interfaces/dto/ToggleMasterRequest.java
import jakarta.validation.constraints.NotNull;

public record ToggleMasterRequest(
        @NotNull Long nifEntidade,
        String actorEmail
) {}
