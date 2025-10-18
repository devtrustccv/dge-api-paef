package cv.gov.dge.paef.domain.formacao.model;
import lombok.*; import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Formacao {
    private String id; private String idAlvara; private String dmTipoFormacao; private String idAreaQualif;
    private String idQualificacao; private String areaDesc; private String qualificacaoDesc;
    private String dmNivelRomano; private String flagCatalogo; private LocalDate dtRegisto;
    private LocalDate dtFimPrev; private LocalDate dtIniPrev; private String dmMetodologia;
    private String selfIdCnq; private String versao;
}
