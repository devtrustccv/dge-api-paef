package cv.gov.dge.paef.application.proconline.service;

import cv.gov.dge.paef.domain.proconline.business.ProcOnlineBusiness;

import cv.gov.dge.paef.domain.proconline.model.ProcOnlineRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// domain/proconline/service/ProcOnlineService.java
@Service
@Transactional(readOnly = true)
public class ProcOnlineService {
    private final ProcOnlineBusiness business;
    public ProcOnlineService(ProcOnlineBusiness business) { this.business = business; }

    public List<ProcOnlineRow> list(String nif) {
        return business.listForUser(nif);
    }
}

