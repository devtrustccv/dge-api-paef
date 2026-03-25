package cv.gov.dge.paef.interfaces.dto.Alvara;

import java.sql.Date;

public interface AlvaraHeaderRow {
    String getIdAlvara();
    String getIdEntidade();
    String getIdProcesso();
    String getNrAlvara();
    String getDescricaoTipoAlvara();
    String getDmSituacao();
    Date getDataEmissao();
    Date getDataPedido();
    Date getDataValidade();
    String getEstadoAlvaraDescricao();
    String getNrProcesso();

    String getIdEstabelecimento();
    String getDesignacao();
    String getLocalEstabelecimento();
    String getEnderecoEstabelecimento();
    String getCaixaPostalEstabelecimento();
    String getEmailEstabelecimento();
    String getTelefoneEstabelecimento();
    String getTelemovelEstabelecimento();

    String getNomeResponsavel();
    String getDmTpDoc();
    String getNrDocResponsavel();
    String getTelemovelResponsavel();
    String getTelefoneResponsavel();
    String getEmailResponsavel();
    String getDmNivelAcademico();
    String getEnderecoResp();
}
