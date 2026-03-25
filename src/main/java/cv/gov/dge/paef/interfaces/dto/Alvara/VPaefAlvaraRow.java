package cv.gov.dge.paef.interfaces.dto.Alvara;

public interface VPaefAlvaraRow {
    String getIdEntidade();
    String getIdAlvara();
    String getIdProcesso();
    String getNrAlvara();
    String getDesignacao();
    String getDescricaoTipoAlvara();
    String getLocalizacao();
    java.sql.Date getDataEmissao();
    java.sql.Date getDataValidade();
    String getDmEstadoAlvara();
    String getEstadoAlvaraDescricao();
    String getDmSituacao();
    String getFlagPublicacao();
    String getDmOrigemReg();
    Boolean getDocAlvaraExists();
}
