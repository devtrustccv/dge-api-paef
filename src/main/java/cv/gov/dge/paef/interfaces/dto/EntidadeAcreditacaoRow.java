package cv.gov.dge.paef.interfaces.dto;

import java.math.BigDecimal;

public interface EntidadeAcreditacaoRow {
    // --- cabeça da entidade ---
    String getEntId();
    String getDenominacao();
    BigDecimal getNif();
    String getNatureza();
    String getEmail();
    String getUrl();
    String getGeogLocalId();

    // --- acreditação ---
    String getCodigoFamilia();
    String getDenominacaoFamilia();
    String getNivel();
    String getSelf();
    String getVersao();
    String getCodigoQualificacao();
    String getDenominacaoQualificacao();
    String getModalidade();
    String getMetodologia();
    String getNrAlvara();
    String getIdAlvara();
    String getStatusAlvara();
    String getFlagCatalogo();
    String getSituacao();
}
