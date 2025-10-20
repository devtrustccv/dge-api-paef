package cv.gov.dge.paef.interfaces.dto;

import java.math.BigDecimal;

public interface EntidadePffpRow {
    String getEntId();
    String getDenominacao();
    String getDenominacaoNorm();
    BigDecimal getNif();
    String getNatureza();
    String getEmail();
    String getUrl();
    String getGeogLocalId();
    String getMorada();
    String getTelemovel();
    String getTelefone();
    String getNrAlvara();
    String getNomePontoFocal();

    String getEmailUser();
    String getFlagMaster();
}
