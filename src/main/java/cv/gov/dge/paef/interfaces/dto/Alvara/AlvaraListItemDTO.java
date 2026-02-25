package cv.gov.dge.paef.interfaces.dto.Alvara;

import lombok.Builder;

@Builder
public record AlvaraListItemDTO(
        String num,
        String estabelecimento,
        String tipoAlvara,
        String localizacao,
        String dtEmissao,
        String dtValidade,
        String estado,
        String situacao,
        String idAlvara,
        String idTask,
        String idEntidade,
        String estadoCod,
        Boolean canViewAlvara,
        Boolean canViewAnexoFormacoes
) {}
