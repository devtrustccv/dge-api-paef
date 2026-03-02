package cv.gov.dge.paef.application.alvara.service;

import cv.gov.dge.paef.application.audit.AlteracaoHistoricoService;
import cv.gov.dge.paef.application.domain.service.DominioService;
import cv.gov.dge.paef.infrastructure.repository.AlvaraRepository;
import cv.gov.dge.paef.infrastructure.repository.EstabelecimentoRepository;
import cv.gov.dge.paef.infrastructure.repository.GeografiaRepository;
import cv.gov.dge.paef.interfaces.dto.Alvara.EstabelecimentoUpdateRequest;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

@Service
public class EstabelecimentoUpdateService {

    private final AlvaraRepository alvaraRepo;
    private final EstabelecimentoRepository estabRepo;
    private final DominioService dominioService;
    private final GeografiaRepository geoQueryRepo; // para nomes na log, opcional
    private final AlteracaoHistoricoService historicoService;     // teu genérico saveHistorico

    public EstabelecimentoUpdateService(
            AlvaraRepository alvaraRepo,
            EstabelecimentoRepository estabRepo,
            DominioService dominioService,
            GeografiaRepository geoQueryRepo,
            AlteracaoHistoricoService historicoService
    ) {
        this.alvaraRepo = alvaraRepo;
        this.estabRepo = estabRepo;
        this.dominioService = dominioService;
        this.geoQueryRepo = geoQueryRepo;
        this.historicoService = historicoService;
    }

    @Transactional
    public ApiResponse<?> update(String idAlvara, EstabelecimentoUpdateRequest req) {

        // 1) validações “pelo menos um contacto”
        if (blank(req.telefone()) && blank(req.telemovel())) {
            return ApiResponse.fail(
                    "É obrigatorio inserir pelo menus um contato do Estabelecimento. Ou o telefone ou o telemovel. Por favor verificar!",
                    null);
        }
        if (blank(req.telefoneResp()) && blank(req.telemovelResp())) {
            return ApiResponse.fail(
                    "É obrigatorio inserir pelo menus um contato do Responsável. Ou o telefone ou o telemovel. Por favor verificar!",
                    null);
        }

        // 2) buscar alvará
        var alv = alvaraRepo.findById(idAlvara).orElse(null);
        if (alv == null || blank(alv.getIdEstabelecimento())) {
            return ApiResponse.fail( "Alvará não encontrado ou sem estabelecimento associado", null);
        }

        // 3) buscar estabelecimento
        var est = estabRepo.findById(alv.getIdEstabelecimento()).orElse(null);
        if (est == null) {
            return ApiResponse.fail( "Estabelecimento não encontrado", null);
        }

        // 4) snapshot OLD (para histórico)
        var oldMap = new LinkedHashMap<String, String>();
        oldMap.put("Nome responsavel", safe(est.getNomeResp()));
        oldMap.put("Tipo documento responsavel", dominioService.getDesc("TIPO_DOCUMENTO", est.getDmTpDoc()).description());
        oldMap.put("Nr. documento resposavel", safe(est.getNrDocResp()));
        oldMap.put("Telefone resposavel", safe(est.getTelefoneResp()));
        oldMap.put("Telemovel resposavel", safe(est.getTelemovelResp()));
        oldMap.put("Email resposavel", safe(est.getEmailResp()));
        oldMap.put("Endereco", safe(est.getEnderecoResp()));
        oldMap.put("Nivel Academico", dominioService.getDesc("NIVEL_ACADEMICO", est.getDmNivelAcademico()).description());
        oldMap.put("Telefone Estabelecimento", est.getTelefone() == null ? "" : est.getTelefone().toString());
        oldMap.put("Telemovel Estabelecimento", est.getTelemovel() == null ? "" : est.getTelemovel().toString());
        oldMap.put("Endereco Estabelecimento", safe(est.getEndereco()));
        oldMap.put("Caixa Postal", safe(est.getCaixaPostal()));

        // geografia (nomes) — usando o query corrigido de self-join
        String oldZonaNome = "";
        String oldFregNome = "";
        String oldConcNome = "";
        String oldIlhaNome = "";
        if (!blank(est.getGeogLocalId())) {
            var geo = geoQueryRepo.findGeoById(est.getGeogLocalId()).orElse(null);
            if (geo != null) {
                oldZonaNome = safe(geo.getZona());
                oldFregNome = safe(geo.getFreguesia());
                oldConcNome = safe(geo.getConcelho());
                oldIlhaNome = safe(geo.getIlha());
            }
        }
        oldMap.put("Zona", oldZonaNome);
        oldMap.put("Freguesia", oldFregNome);
        oldMap.put("ilha", oldIlhaNome);
        oldMap.put("Concelho", oldConcNome);

        // 5) aplicar updates
        est.setTelefone(toBigDecimalOrNull(req.telefone()));
        est.setTelemovel(toBigDecimalOrNull(req.telemovel()));
        est.setEndereco(req.endereco());
        est.setCaixaPostal(req.caixaPostal());
        est.setGeogLocalId(req.zonaId()); // pode ser null se permitires

        est.setNomeResp(req.nomeResp());
        est.setDmTpDoc(req.tipoDocumentoResp());
        est.setNrDocResp(req.nrDocumentoResp());
        est.setTelemovelResp(req.telemovelResp());
        est.setTelefoneResp(req.telefoneResp());
        est.setEmailResp(req.emailResp());
        est.setEnderecoResp(req.enderecoResp());
        est.setDmNivelAcademico(req.nivelFormacaoAcademica());

        estabRepo.save(est);

        // 6) snapshot NEW
        var newMap = new LinkedHashMap<String, String>();
        newMap.put("Nome responsavel", safe(est.getNomeResp()));
        newMap.put("Tipo documento responsavel", dominioService.getDesc("TIPO_DOCUMENTO", est.getDmTpDoc()).description());
        newMap.put("Nr. documento resposavel", safe(est.getNrDocResp()));
        newMap.put("Telefone resposavel", safe(est.getTelefoneResp()));
        newMap.put("Telemovel resposavel", safe(est.getTelemovelResp()));
        newMap.put("Email resposavel", safe(est.getEmailResp()));
        newMap.put("Endereco", safe(est.getEnderecoResp()));
        newMap.put("Nivel Academico", dominioService.getDesc("NIVEL_ACADEMICO", est.getDmNivelAcademico()).description());
        newMap.put("Telefone Estabelecimento", est.getTelefone() == null ? "" : est.getTelefone().toString());
        newMap.put("Telemovel Estabelecimento", est.getTelemovel() == null ? "" : est.getTelemovel().toString());
        newMap.put("Endereco Estabelecimento", safe(est.getEndereco()));
        newMap.put("Caixa Postal", safe(est.getCaixaPostal()));

        String newZonaNome = "";
        String newFregNome = "";
        String newConcNome = "";
        String newIlhaNome = "";
        if (!blank(est.getGeogLocalId())) {
            var geo2 = geoQueryRepo.findGeoById(est.getGeogLocalId()).orElse(null);
            if (geo2 != null) {
                newZonaNome = safe(geo2.getZona());
                newFregNome = safe(geo2.getFreguesia());
                newConcNome = safe(geo2.getConcelho());
                newIlhaNome = safe(geo2.getIlha());
            }
        }
        newMap.put("Zona", newZonaNome);
        newMap.put("Freguesia", newFregNome);
        newMap.put("ilha", newIlhaNome);
        newMap.put("Concelho", newConcNome);

        // 7) salvar histórico apenas se houver mudanças
        // (o teu método genérico já compara campo a campo e só grava se diferente)
        historicoService.saveHistorico(
                newMap,
                oldMap,
                "ALVARA",
                alv.getId(),
                safe(est.getDesignacao()),
                "",                 // motivo
                safe(req.actorEmail())
        );

        return ApiResponse.ok( "Estabelecimento atualizado com sucesso", null);
    }

    private static String safe(String s) { return s == null ? "" : s; }
    private static boolean blank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static BigDecimal toBigDecimalOrNull(String s) {
        if (blank(s)) return null;
        return new BigDecimal(s.trim());
    }

}