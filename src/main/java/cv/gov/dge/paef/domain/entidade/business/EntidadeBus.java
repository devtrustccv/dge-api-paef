package cv.gov.dge.paef.domain.entidade.business;

import cv.gov.dge.paef.application.entidade.dto.EntidadeDTO;
import cv.gov.dge.paef.application.entidade.service.EntidadeService;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.infrastructure.mapper.EntidadeMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class EntidadeBus {
    private final EntidadeService service;
    private final EntidadeMapper mapper;
    public EntidadeBus(EntidadeService service, EntidadeMapper mapper){ this.service=service; this.mapper=mapper; }

    @Transactional
    public EntidadeEntity create(EntidadeDTO dto){
        return service.save(mapper.toEntity(dto));
    }

    @Component
    public class NaturezaResolver {
        private static final Map<String, String> MAP = Map.of(
                "PRIV", "Privada",
                "PUB", "Pública",
                "ONG", "ONG"
                // adiciona os teus valores reais aqui
        );

        public String resolve(String dmNatureza) {
            if (dmNatureza == null) return "";
            return MAP.getOrDefault(dmNatureza, dmNatureza);
        }
}}
