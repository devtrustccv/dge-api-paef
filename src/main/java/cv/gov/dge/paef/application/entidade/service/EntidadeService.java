package cv.gov.dge.paef.application.entidade.service;

import cv.gov.dge.paef.application.audit.AlteracaoHistoricoService;
import cv.gov.dge.paef.domain.geografia.business.GeografiaBusiness;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeInfoDTO;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeUpdateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@Service @Transactional
public class EntidadeService {
    private final EntidadeRepository repo;
    private final GeografiaBusiness geoResolver;
    private final AlteracaoHistoricoService historico;

    public EntidadeService(
            EntidadeRepository repo,
            GeografiaBusiness geoResolver,
            AlteracaoHistoricoService historico
                           ){
        this.repo = repo;
        this.geoResolver = geoResolver;
        this.historico = historico;
    }
    private static final String TP_REL_ENTIDADE = "ENTIDADE";

    public EntidadeEntity save(EntidadeEntity e){ return repo.save(e); }

    @Transactional(readOnly = true)
    public Optional<EntidadeEntity> findByNif(BigDecimal nif){
        return repo.findByNif(nif);
    }

    @Transactional
    public EntidadeInfoDTO updateByNif(Long nif, EntidadeUpdateDTO dto) {
        var ent = repo.findByNif(new BigDecimal(nif)).orElse(null);
        if (ent == null) return null;

        // BEFORE snapshot (com geografia atual)
        var geoBefore = (ent.getGeogLocalId() == null || ent.getGeogLocalId().isBlank())
                ? new GeografiaBusiness.GeoFields("", "", "", "")
                : geoResolver.resolve(ent.getGeogLocalId());LinkedHashMap<String, String> before = snapshot(ent, geoBefore);

        // apply changes
        if (dto.zonaId() != null && !dto.zonaId().isBlank()) {
            ent.setGeogLocalId(dto.zonaId());
        }
        if (dto.telefone() != null && !dto.telefone().isBlank())
            ent.setTelefone(dto.telefone());
        if (dto.telemovel() != null && !dto.telemovel().isBlank())
            ent.setTelemovel(dto.telemovel());
        if (dto.caixaPostal() != null && !dto.caixaPostal().isBlank())
            ent.setCaixaPostal(dto.caixaPostal());
        if (dto.email() != null && !dto.email().isBlank())
            ent.setEmail(dto.email());
        if (dto.endereco() != null && !dto.endereco().isBlank())
            ent.setEndereco(dto.endereco());
        ent.setSendedToPffp(false);

        repo.save(ent);

        // AFTER snapshot (com geografia nova)
        var geoAfter = (ent.getGeogLocalId() == null || ent.getGeogLocalId().isBlank())
                ? new GeografiaBusiness.GeoFields("", "", "", "")
                : geoResolver.resolve(ent.getGeogLocalId());
        LinkedHashMap<String, String> after = snapshot(ent, geoAfter);

        // save historico (somente diffs)
        historico.saveHistorico(
                before,
                after,
                TP_REL_ENTIDADE,
                ent.getId(),
                ent.getDenominacaoSocial(),
                dto.motivo() == null ? "" : dto.motivo(),
                dto.userEmail() == null ? "" : dto.userEmail()
        );

        // monta resposta (podes reutilizar teu EntidadeInfoService se já existir)
        return EntidadeInfoDTO.builder()
                .denominacao_form1(ent.getDenominacaoSocial())
                .nif_form(ent.getNif() == null ? "" : ent.getNif().toPlainString())
                .matricularegisto_comercial(ent.getNrMatricula() == null ? "" : ent.getNrMatricula())
                .data_constituicao(ent.getDtConstituicao() == null ? "" : ent.getDtConstituicao().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .natureza_form(ent.getDmNatureza() == null ? "" : ent.getDmNatureza()) // se tiver resolver, troca
                .representante_legal(ent.getRepresentanteLegal() == null ? "" : ent.getRepresentanteLegal())
                .ilha(geoAfter.ilha())
                .concelho_id(geoAfter.concelho())
                .freguesia_id(geoAfter.freguesia())
                .zona_id(ent.getGeogLocalId() == null ? "" : ent.getGeogLocalId())
                .id_localizacao(ent.getGeogLocalId() == null ? "" : ent.getGeogLocalId())
                .local("") // se quiser: nome do registo geografia; dá pra resolver igual antes
                .endereco(ent.getEndereco() == null ? "" : ent.getEndereco())
                .caixa_postal(ent.getCaixaPostal() == null ? "" : ent.getCaixaPostal())
                .telefone(ent.getTelefone() == null ? "" : ent.getTelefone())
                .telemovel(ent.getTelemovel() == null ? "" : ent.getTelemovel())
                .email(ent.getEmail() == null ? "" : ent.getEmail())
                .build();
    }

    private LinkedHashMap<String, String> snapshot(Object entObj, GeografiaBusiness.GeoFields geo) {
        // entObj é EntidadeEntity; mantive Object para não “importar” aqui.
        var ent = (cv.gov.dge.paef.infrastructure.EntidadeEntity) entObj;

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("Ilha", geo.ilha());
        map.put("Concelho", geo.concelho());
        map.put("Freguesia", geo.freguesia());
        map.put("Zona", geo.zona());
        map.put("Telefone", nvl(ent.getTelefone()));
        map.put("Telemovel", nvl(ent.getTelemovel()));
        map.put("Email", nvl(ent.getEmail()));
        map.put("Endereco", nvl(ent.getEndereco()));
        map.put("Caixa Postal", nvl(ent.getCaixaPostal()));
        return map;
    }

    private String nvl(String s){ return s == null ? "" : s; }
}
