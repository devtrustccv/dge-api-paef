// src/main/java/cv/gov/dge/paef/domain/entidade/service/EntidadeInfoService.java
package cv.gov.dge.paef.domain.entidade.service;

import cv.gov.dge.paef.domain.entidade.business.EntidadeBus;
import cv.gov.dge.paef.domain.geografia.business.GeografiaBusiness;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.GeografiaRepository;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeInfoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
public class EntidadeInfoService {

    private final EntidadeRepository entidadeRepo;
    private final GeografiaRepository geografiaRepo;
    private final EntidadeBus.NaturezaResolver  naturezaResolver;
    private final GeografiaBusiness.LocalizacaoResolver localResolver;

    public EntidadeInfoService(
            EntidadeRepository entidadeRepo,
            GeografiaRepository geografiaRepo,
            EntidadeBus.NaturezaResolver naturezaResolver,
            GeografiaBusiness.LocalizacaoResolver localResolver
    ) {
        this.entidadeRepo = entidadeRepo;
        this.geografiaRepo = geografiaRepo;
        this.naturezaResolver = naturezaResolver;
        this.localResolver = localResolver;
    }

    public EntidadeInfoDTO getByNif(BigDecimal nif) {
        var ent = entidadeRepo.findByNif(nif).orElse(null);
        if (ent == null) return null;

        var geog = (ent.getGeogLocalId() == null) ? null
                : geografiaRepo.findById(ent.getGeogLocalId()).orElse(null);

        String dataConst = "";
        if (ent.getDtConstituicao() != null) {
            dataConst = ent.getDtConstituicao().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        return EntidadeInfoDTO.builder()
                .denominacao_form1(ent.getDenominacaoSocial())
                .nif_form(ent.getNif() == null ? "" : ent.getNif().toPlainString())
                .matricularegisto_comercial(ent.getNrMatricula() == null ? "" : ent.getNrMatricula())
                .data_constituicao(dataConst)
                .natureza_form(naturezaResolver.resolve(ent.getDmNatureza()))
                .representante_legal(ent.getRepresentanteLegal() == null ? "" : ent.getRepresentanteLegal())
                .ilha(geog != null && geog.getIlha() != null ? geog.getIlha() : "")
                .concelho_id(geog != null && geog.getConcelho() != null ? geog.getConcelho() : "")
                .freguesia_id(geog != null && geog.getFreguesia() != null ? geog.getFreguesia() : "")
                .zona_id(ent.getGeogLocalId() == null ? "" : ent.getGeogLocalId())
                .id_localizacao(ent.getGeogLocalId() == null ? "" : ent.getGeogLocalId())
                .local(localResolver.resolveLocal(ent.getGeogLocalId()))
                .endereco(ent.getEndereco() == null ? "" : ent.getEndereco())
                .caixa_postal(ent.getCaixaPostal() == null ? "" : ent.getCaixaPostal())
                .telefone(ent.getTelefone() == null ? "" : ent.getTelefone())
                .telemovel(ent.getTelemovel() == null ? "" : ent.getTelemovel())
                .email(ent.getEmail() == null ? "" : ent.getEmail())
                .build();
    }
}
