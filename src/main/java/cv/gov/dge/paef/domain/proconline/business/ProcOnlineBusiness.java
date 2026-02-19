package cv.gov.dge.paef.domain.proconline.business;

import cv.gov.dge.paef.domain.proconline.model.ProcOnlineRow;
import cv.gov.dge.paef.infrastructure.PaefTProcOnlineEntity;
import cv.gov.dge.paef.infrastructure.mapper.ProcOnlineMapper;
import cv.gov.dge.paef.infrastructure.repository.ProcOnlineRepository;
import org.springframework.stereotype.Component;

import java.util.List;

// domain/proconline/business/ProcOnlineBusiness.java
@Component
public class ProcOnlineBusiness {

    private final ProcOnlineRepository procRepo;
    private final ProcOnlineMapper mapper;

    public ProcOnlineBusiness(
            ProcOnlineRepository procRepo,
            ProcOnlineMapper mapper
    ) {
        this.procRepo = procRepo;
        this.mapper = mapper;
    }

    public List<ProcOnlineRow> listForUser(String nif) {

        List<PaefTProcOnlineEntity> procs = nif!=null && !nif.isBlank()
                ? procRepo.findAll()
                : procRepo.findByIdTpProcesso("PED_REG");

        return procs.stream()
                .map(p -> mapper.toModel(p, ProcessTypeResolver.resolveName(p.getIdTpProcesso())))
                .toList();
    }
    @Component
    public class ProcessTypeResolver {
        public static String resolveName(String idTpProcesso) {
            // TODO: ligar ao teu catálogo real (tabela/domínio/config)
            // por agora: devolve o próprio id como fallback
            return idTpProcesso == null ? "" : idTpProcesso;
        }
    }
}

