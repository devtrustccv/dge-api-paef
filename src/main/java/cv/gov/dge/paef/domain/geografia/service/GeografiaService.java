// src/main/java/cv/gov/dge/paef/domain/geografia/service/GeografiaService.java
package cv.gov.dge.paef.domain.geografia.service;

import cv.gov.dge.paef.domain.geografia.business.GeografiaBusiness;
import cv.gov.dge.paef.domain.geografia.model.GeografiaOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GeografiaService {

    private final GeografiaBusiness business;

    public GeografiaService(GeografiaBusiness business) {
        this.business = business;
    }

    public List<GeografiaOption> ilhas() {
        return business.getIlhas();
    }

    public List<GeografiaOption> concelhos(String ilha) {
        return business.getConcelhosByIlha(ilha);
    }

    public List<GeografiaOption> freguesias(String concelho) {
        return business.getFreguesiasByConcelho(concelho);
    }

    public List<GeografiaOption> zonas(String freguesia) {
        return business.getZonasByFreguesia(freguesia);
    }
}
