package cv.gov.dge.paef.interfaces.dto.ContaAcesso;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EstadoContaResolver {

    private static final Map<String, String> MAP = Map.of(
            "A", "Ativa",
            "I", "Inativa"
    );

    public String resolve(String dmEstadoConta) {
        if (dmEstadoConta == null) return "";
        return MAP.getOrDefault(dmEstadoConta, dmEstadoConta);
    }
}