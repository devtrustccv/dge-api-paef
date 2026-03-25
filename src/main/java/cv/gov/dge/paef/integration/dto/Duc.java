package cv.gov.dge.paef.integration.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Duc {
    private String duc;
    private String entidade;
    private String referencia;
    private double valor;
    private String estado;
    private String estadoDescricao;
    private String link;
}
