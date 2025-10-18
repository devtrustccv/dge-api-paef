package cv.gov.dge.paef.domain.alvara.model;
import lombok.*; import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alvara {
    private String id; private String idEntidade; private String idPedido; private String idTipoAlvara;
    private String idProcesso; private String dmEstadoAlvara; private LocalDate dataPedido;
    private LocalDate dataEmissao; private LocalDate dataValidade; private String idEstabelecimento;
    private String nrProcesso; private String dmTipoValidade; private LocalDate dataDespacho;
    private String observacao; private String dmSituacao; private String parceria; private String nrAlvara;
    private String dmOrigemReg; private LocalDate dtEnvIncv; private String referenciaBo; private LocalDate dtPubBo;
    private String obsBo; private String linkReportAlvara; private String flagPublicacao; private String contraProva;
}

