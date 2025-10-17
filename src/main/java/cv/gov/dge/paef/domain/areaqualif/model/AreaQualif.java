package cv.gov.dge.paef.domain.areaqualif.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AreaQualif {
    private String id;              // codigoQualif
    private String idPai;           // codigoFamilia
    private String siglaCodigo;     // codigoQualif
    private String dmNivelArabico;  // nivel
    private String descricao;       // denominacaoQualif
    private String estado;          // 'A'
    private String selfIdCnq;       // selfId
    private String versao;          // versao
}
