package cv.gov.dge.paef.domain.contaacesso.business;

import cv.gov.dge.paef.infrastructure.repository.ContaAcessoRepository;
import cv.gov.dge.paef.interfaces.dto.ContaAcesso.ContaAcessoEntidadeDTO;
import cv.gov.dge.paef.interfaces.dto.ContaAcesso.EstadoContaResolver;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeContaDTO;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ContaAcessoBusiness {

    private final ContaAcessoRepository repository;
    private final EstadoContaResolver estadoResolver;

    public ContaAcessoBusiness(ContaAcessoRepository repository,
                               EstadoContaResolver estadoResolver) {
        this.repository = repository;
        this.estadoResolver = estadoResolver;
    }

    public boolean isUserMaster(String emailUser, BigDecimal nifEntidade) {

        if (emailUser == null || nifEntidade == null)
            return false;

        return repository.existsMasterByNifAndEmail(
                nifEntidade,
                emailUser
        );
    }

    public List<EntidadeContaDTO> listEntidadesByUser(String emailUser) {

        var rows = repository.findEntidadesByUser(emailUser);

        return rows.stream()
                .map(r -> EntidadeContaDTO.builder()
                        .nif(r.getNif())
                        .denominacao(r.getDenominacao())
                        .flagMaster(r.getFlagMaster())
                        .build())
                .toList();
    }

    public List<ContaAcessoEntidadeDTO> listByNif(Long nif) {
        var rows = repository.findAllByEntidadeNif(new BigDecimal(nif));
        var fmt = DateTimeFormatter.ISO_LOCAL_DATE;

        return rows.stream().map(r -> {
            boolean isMaster = "SIM".equalsIgnoreCase(r.getFlagMaster());

            return ContaAcessoEntidadeDTO.builder()
                    .id(r.getId())
                    .utilizador(r.getEmailUser())
                    .principalMaster(isMaster ? 1 : 0)
                    .principalMasterCheck(isMaster ? 1 : 2)
                    .dataRegisto(r.getDataRegisto() == null ? null : r.getDataRegisto().format(fmt))
                    .estado(estadoResolver.resolve(r.getDmEstadoConta()))
                    .build();
        }).toList();
    }
}
