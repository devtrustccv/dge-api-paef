package cv.gov.dge.paef.interfaces.dto.Qualificacao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FormacaoOutroDto {

    private String idAlvara;
    private String nrAlvara;
    private String entidadeNome;

    private String familia;
    private String qualificacao;
    private String nivel;
    private String idFormacao;
}
