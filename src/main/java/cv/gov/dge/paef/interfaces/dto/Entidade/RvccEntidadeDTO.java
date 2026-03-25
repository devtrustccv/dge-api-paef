package cv.gov.dge.paef.interfaces.dto.Entidade;

import cv.gov.dge.paef.interfaces.dto.Qualificacao.RvccQualificacaoDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record RvccEntidadeDTO(
        String nif,
        String designacaoComercial,
        String ilha,
        String concelho,
        String idConselho,
        String endereco,
        String numAlvara,
        String estadoAlvara,
        List<RvccQualificacaoDTO> qualificacoesAtivas,
        List<RvccQualificacaoDTO> qualificacoesInativas
) {}
