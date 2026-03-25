// src/main/java/cv/gov/dge/paef/interfaces/dto/EntidadeInfoDTO.java
package cv.gov.dge.paef.interfaces.dto.Entidade;

import lombok.Builder;

@Builder
public record EntidadeInfoDTO(
        String denominacao,
        String nif,
        String matriculaRegistoComercial,
        String dataConstituicao,
        String natureza,
        String representanteLegal,
        String ilha,
        String concelhoId,
        String freguesiaId,
        String zonaId,
        String idLocalizacao,
        String local,
        String endereco,
        String caixaPostal,
        String telefone,
        String telemovel,
        String email
) {}
