package cv.gov.dge.paef.interfaces.dto.notificacao;

import java.time.LocalDate;

public interface NotificacaoRow {
    String getId();
    String getAssunto();
    LocalDate getDataRegistro();
    String getTipo();
    String getIdProcesso();
    String getIdPaginaEtapa();
    String getNrAlvara();
}
