package cv.gov.dge.paef.application.contaacesso.service;

import cv.gov.dge.paef.domain.contaacesso.business.ContaAcessoBusiness;
import cv.gov.dge.paef.interfaces.dto.ContaAcesso.ContaAcessoEntidadeDTO;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeContaDTO;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContaAcessoService {

    private final ContaAcessoBusiness business;

    public ContaAcessoService(ContaAcessoBusiness business) {
        this.business = business;
    }

    public boolean isUserMaster(String emailUser, BigDecimal nifEntidade) {
        return business.isUserMaster(emailUser, nifEntidade);
    }


    public List<EntidadeContaDTO> listEntidades(String emailUser) {
        return business.listEntidadesByUser(emailUser);
    }

    public List<ContaAcessoEntidadeDTO> list(Long nif) {
        return business.listByNif(nif);
    }
}