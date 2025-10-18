package cv.gov.dge.paef.domain.entidade.model;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Entidade {
    private String id; private String denominacaoSocial; private String denominacaoComercial;
    private BigDecimal nif; private String dmNatureza; private LocalDate dtConstituicao;
    private String nrMatricula; private String endereco; private String caixaPostal;
    private String telefone; private String telemovel; private String email; private String url;
    private String representanteLegal; private String dmOrigemReg; private LocalDate dataRegisto;
    private Long userRegistoId; private String abreviatura; private String geogLocalId;
    private String denominacaoSocialNorm; private Boolean isOnPortal;
}
