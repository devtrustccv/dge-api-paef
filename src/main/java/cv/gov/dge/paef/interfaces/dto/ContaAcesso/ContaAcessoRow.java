package cv.gov.dge.paef.interfaces.dto.ContaAcesso;

// infrastructure/projection/ContaAcessoRow.java
import java.time.LocalDate;

public interface ContaAcessoRow {
    String getId();
    String getEmailUser();
    String getFlagMaster();
    LocalDate getDataRegisto();
    String getDmEstadoConta();
}
