package cv.gov.dge.paef.interfaces.dto.logs;

import java.sql.Date;
import java.math.BigDecimal;

public interface LogAlteracaoRow {
    Date getDataRegisto();
    String getUserRegistoId();
    String getCampoAlt();
    String getValorAnterior();
    String getValorAtual();
}
