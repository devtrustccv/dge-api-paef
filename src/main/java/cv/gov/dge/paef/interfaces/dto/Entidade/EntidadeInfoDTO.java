// src/main/java/cv/gov/dge/paef/interfaces/dto/EntidadeInfoDTO.java
package cv.gov.dge.paef.interfaces.dto.Entidade;

import lombok.Builder;

@Builder
public record EntidadeInfoDTO(
        String denominacao_form1,
        String nif_form,
        String matricularegisto_comercial,
        String data_constituicao,
        String natureza_form,
        String representante_legal,
        String ilha,
        String concelho_id,
        String freguesia_id,
        String zona_id,
        String id_localizacao,
        String local,
        String endereco,
        String caixa_postal,
        String telefone,
        String telemovel,
        String email
) {}
