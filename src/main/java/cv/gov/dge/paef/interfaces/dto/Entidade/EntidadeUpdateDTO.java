// src/main/java/cv/gov/dge/paef/interfaces/dto/EntidadeUpdateDTO.java
package cv.gov.dge.paef.interfaces.dto.Entidade;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record EntidadeUpdateDTO(
        @JsonProperty("zona_id") String zonaId,   // geog_local_id novo
        String telefone,
        String telemovel,
        String email,
        String endereco,
        @JsonProperty("caixa_postal") String caixaPostal,
        @JsonProperty("user_email") String userEmail,      // para log
        String motivo                                   // opcional para log
) {}
