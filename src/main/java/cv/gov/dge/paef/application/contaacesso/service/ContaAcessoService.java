package cv.gov.dge.paef.application.contaacesso.service;

import cv.gov.dge.paef.application.audit.AlteracaoHistoricoService;
import cv.gov.dge.paef.domain.contaacesso.business.ContaAcessoBusiness;
import cv.gov.dge.paef.infrastructure.ContaAcessoEntity;
import cv.gov.dge.paef.infrastructure.repository.ContaAcessoRepository;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.ContaAcesso.ContaAcessoEntidadeDTO;
import cv.gov.dge.paef.interfaces.dto.Entidade.EntidadeContaDTO;
import cv.gov.dge.paef.interfaces.dto.EnvelopeData;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ContaAcessoService {

    private final ContaAcessoBusiness business;
    private final ContaAcessoRepository contaRepo;
    private final EntidadeRepository entidadeRepo;
    private final AlteracaoHistoricoService historico;

    private static final String SIM = "SIM";
    private static final String NAO = "NAO";
    private static final String TP_REL_CONTA = "CONTA_ACESSO";
    private static final String ATIVO = "A";     // getConfig.ATIVO
    private static final String INATIVO = "I";   // getConfig.INATIVO
    private static final String ORIGEM_ONLINE = "ONLINE";

    public ContaAcessoService(ContaAcessoBusiness business,
                              ContaAcessoRepository contaRepo,
                              EntidadeRepository entidadeRepo,
                              AlteracaoHistoricoService historico
                              ) {
        this.business = business;
        this.contaRepo = contaRepo;
        this.entidadeRepo = entidadeRepo;
        this.historico = historico;
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

    @Transactional
    public ApiResponse<?> toggleMaster(String idConta, Long nifEntidade, String actorEmail) {

        var conta = contaRepo.findById(idConta).orElse(null);
        if (conta == null) {
            return new ApiResponse<>(false, "Conta não encontrada", null);
        }

        // 1) Resolver idEntidade via NIF (para garantir que estamos a operar na entidade certa)
        var entidade = entidadeRepo.findByNif(new BigDecimal(nifEntidade)).orElse(null);
        if (entidade == null) {
            return ApiResponse.fail("Entidade não encontrada para o NIF informado", null);
        }

        String idEntidade = entidade.getId();

        // (opcional mas recomendado) garantir que a conta pertence a essa entidade
        if (conta.getIdEntidade() == null || !conta.getIdEntidade().equals(idEntidade)) {
            return ApiResponse.fail("A conta não pertence à entidade indicada", null);
        }

        // Se já é master -> mantém (idempotente)
        if (SIM.equalsIgnoreCase(conta.getFlagMaster())) {
            String valorAnt = conta.getFlagMaster() == null ? "" : conta.getFlagMaster();
            String valorAtual = SIM;

            // opcional: não gravar histórico, porque não houve mudança
            return ApiResponse.ok(
                    "Conta já é master (sem alterações)",
                    java.util.Map.of("id", idConta, "flagMaster", valorAtual)
            );
        }

// Se não é master -> promove e garante único master
        String valorAnt = conta.getFlagMaster() == null ? "" : conta.getFlagMaster();

// despromove outras masters da mesma entidade
        var outras = contaRepo.findByIdEntidadeAndIdNot(idEntidade, idConta);
        for (var co : outras) {
            if (SIM.equalsIgnoreCase(co.getFlagMaster())) {
                co.setFlagMaster(NAO);
                contaRepo.save(co);
            }
        }

        conta.setFlagMaster(SIM);
        contaRepo.save(conta);

        String valorAtual = SIM;

// histórico só se mudou
        if (!valorAnt.equals(valorAtual)) {
            LinkedHashMap<String, String> before = new LinkedHashMap<>();
            LinkedHashMap<String, String> after = new LinkedHashMap<>();
            before.put("Flag. Master", valorAnt);
            after.put("Flag. Master", valorAtual);

            historico.saveHistorico(
                    before, after,
                    TP_REL_CONTA,
                    idConta,
                    conta.getEmailUser() == null ? "" : conta.getEmailUser(),
                    "",
                    actorEmail == null ? "" : actorEmail
            );
        }

        return ApiResponse.ok(
                "Flag master atualizada",
                java.util.Map.of("id", idConta, "flagMaster", valorAtual)
        );
    }

    @Transactional
    public ApiResponse<?> toggleEstado(String id, String actorEmail) {

        var c = contaRepo.findById(id).orElse(null);
        if (c == null) {
            return ApiResponse.fail( "Conta não encontrada", null);
        }

        String oldEstado = nvl(c.getDmEstadoConta());
        String newEstado;

        if (ATIVO.equalsIgnoreCase(oldEstado)) newEstado = INATIVO;
        else if (INATIVO.equalsIgnoreCase(oldEstado)) newEstado = ATIVO;
        else {
            // estado desconhecido -> não mexe (ou define default)
            return ApiResponse.fail("Estado inválido na conta: " + oldEstado, null);
        }

        c.setDmEstadoConta(newEstado);

        // regra: se é master, ao alterar estado (e especialmente ao inativar) deve deixar de ser master
        if (SIM.equalsIgnoreCase(nvl(c.getFlagMaster()))) {
            c.setFlagMaster(NAO);
        }

        contaRepo.save(c);

        // histórico só se mudou
        if (!oldEstado.equalsIgnoreCase(newEstado)) {
            LinkedHashMap<String, String> before = new LinkedHashMap<>();
            LinkedHashMap<String, String> after = new LinkedHashMap<>();
            before.put("Estado da Conta", oldEstado);
            after.put("Estado da Conta", newEstado);

            historico.saveHistorico(
                    before,
                    after,
                    TP_REL_CONTA,
                    id,
                    nvl(c.getEmailUser()),
                    "",
                    nvl(actorEmail).isBlank() ? nvl(c.getEmailUser()) : actorEmail
            );
        }

        return ApiResponse.ok(
                "Estado atualizado",
                Map.of("id", id, "dmEstadoConta", c.getDmEstadoConta(), "flagMaster", c.getFlagMaster())
        );
    }

    @Transactional
    public ApiResponse<?> associar(Long nif, String emailUser, String actorEmail) {

        var ent = entidadeRepo.findByNif(new BigDecimal(nif)).orElse(null);
        if (ent == null) {
            return ApiResponse.fail("Entidade não encontrada para o NIF informado", null);
        }

        String idEntidade = ent.getId();

        // regra: não permitir duplicado por entidade + email
        boolean exists = contaRepo.existsByIdEntidadeAndEmailUserIgnoreCase(idEntidade, emailUser);
        if (exists) {
            return ApiResponse.fail(
                    "Já existe uma conta com este email para esta entidade. Por Favor Verificar!",
                    null);
        }

        var c = new ContaAcessoEntity();
       // c.setId(UUID.randomUUID().toString()); // se o teu ID for gerado por @GenericGenerator, remove esta linha
        c.setIdEntidade(idEntidade);
        c.setEmailUser(emailUser);
        c.setFlagMaster(NAO);
        c.setDmEstadoConta(ATIVO);
        c.setDataRegisto(LocalDate.now());
        c.setUserRegisto(actorEmail == null ? "" : actorEmail);
        c.setDmOrigemReg(ORIGEM_ONLINE);

        contaRepo.save(c);

        return  ApiResponse.ok(

                "Conta associada com sucesso",
                Map.of(
                        "id", c.getId(),
                        "emailUser", c.getEmailUser(),
                        "flagMaster", c.getFlagMaster(),
                        "dmEstadoConta", c.getDmEstadoConta()
                )
        );
    }

    private String nvl(String s) { return s == null ? "" : s; }
}