// src/main/java/cv/gov/dge/paef/interfaces/dto/FormacaoDTO.java
package cv.gov.dge.paef.interfaces.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record FormacaoDTO(
        String id,
        @NotBlank String idAlvara,
        @NotBlank String dmTipoFormacao,
        @NotBlank String idAreaQualif,
        @NotBlank String idQualificacao,
        String areaDesc,
        String qualificacaoDesc,
        String dmNivelRomano,
        @NotBlank String flagCatalogo,
        LocalDate dtRegisto,
        LocalDate dtFimPrev,
        LocalDate dtIniPrev,
        String dmMetodologia,
        String selfIdCnq,
        String versao
) {}
