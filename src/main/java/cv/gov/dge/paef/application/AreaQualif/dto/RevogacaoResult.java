package cv.gov.dge.paef.application.AreaQualif.dto;

public record RevogacaoResult(
        boolean qualificacaoRevogada,
        int acreditacoesInativadas,
        int acreditacoesPendentesCriadas
) {}
