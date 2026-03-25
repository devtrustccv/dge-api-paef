package cv.gov.dge.paef.interfaces.dto.Qualificacao;

public interface RvccFormacaoRow {
    String getIdFormacao();
    String getNif();
    String getDesignacaoComercial();
    String getIlhaNome();
    String getConcelhoNome();
    String getConcelhoId();
    String getEndereco();
    String getNumAlvara();
    String getEstadoAlvara();

    String getCodigoCnq();
    String getSelfidQp();
    String getDenominacao();
    String getFamilia();
    String getCodigoFamilia();
    String getNivelQnq();

    Boolean getFlagRvcc();
    Boolean getSendedToRvcc();
}