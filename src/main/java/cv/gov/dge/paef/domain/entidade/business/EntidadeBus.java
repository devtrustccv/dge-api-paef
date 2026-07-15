package cv.gov.dge.paef.domain.entidade.business;

import cv.gov.dge.paef.application.entidade.service.EntidadeService;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeCreateRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Component
public class EntidadeBus {
    private final EntidadeService service;
    public EntidadeBus(EntidadeService service){ this.service=service; }

    @Transactional
    public CreateResult create(EntidadeCreateRequestDTO dto){
        var nif = BigDecimal.valueOf(dto.nif());
        var existing = service.findByNif(nif);
        if (existing.isPresent()) return new CreateResult(existing.get(), false);

        var entity = EntidadeEntity.builder()
                .denominacaoSocial(dto.denominacaoSocial())
                .denominacaoComercial(dto.denominacaoComercial())
                .nif(nif)
                .dmNatureza(dto.dmNatureza())
                .dtConstituicao(dto.dtConstituicao())
                .nrMatricula(dto.nrMatricula())
                .endereco(dto.endereco())
                .caixaPostal(dto.caixaPostal())
                .telefone(dto.telefone())
                .telemovel(dto.telemovel())
                .email(dto.email())
                .url(dto.url())
                .representanteLegal(dto.representanteLegal())
                .dmOrigemReg("PORTAL")
                .dataRegisto(LocalDate.now())
                .userRegistoId(BigDecimal.valueOf(61))
                .abreviatura(dto.abreviatura())
                .geogLocalId(dto.geogLocalId())
                .denominacaoSocialNorm(dto.denominacaoSocialNorm())
                .isOnPortal(false)
                .build();
        entity.setSendedToPffp(false);
        return new CreateResult(service.save(entity), true);
    }

    public record CreateResult(EntidadeEntity entity, boolean created) {}

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
