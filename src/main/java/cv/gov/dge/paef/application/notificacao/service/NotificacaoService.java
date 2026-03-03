package cv.gov.dge.paef.application.notificacao.service;

import cv.gov.dge.paef.helpers.PeriodoParser;
import cv.gov.dge.paef.helpers.Utils;
import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.infrastructure.TNotificacaoEntity;
import cv.gov.dge.paef.infrastructure.repository.AlvaraRepository;
import cv.gov.dge.paef.infrastructure.repository.EntidadeRepository;
import cv.gov.dge.paef.infrastructure.repository.NotificacaoRepository;
import cv.gov.dge.paef.interfaces.dto.ApiResponse;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoListItemDTO;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoRow;
import cv.gov.dge.paef.interfaces.dto.notificacao.NotificacaoSpecs;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class NotificacaoService {

    private final NotificacaoRepository repo;
    private final EntidadeRepository entidadeRepo; // findByNif
    private final AlvaraRepository alvaraRepo;
    //private final ProcessoService processoService; // resolver nome do processo pelo idProcesso

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public NotificacaoService(NotificacaoRepository repo,
                              EntidadeRepository entidadeRepo,
                              AlvaraRepository alvaraRepo//,
                              //ProcessoService processoService
    ) {
        this.repo = repo;
        this.entidadeRepo = entidadeRepo;
        this.alvaraRepo = alvaraRepo;
       // this.processoService = processoService;
    }

    public ApiResponse<?> listar(BigDecimal nif, String alvara, String periodo, Integer limit) {

        var ent = entidadeRepo.findByNif(nif).orElse(null);
        if (ent == null) {
            return ApiResponse.fail( "Entidade não encontrada para o NIF informado", null);
        }

        if(Utils.blank(limit.toString()))
            limit=100;
        LocalDate[] range = PeriodoParser.parse(periodo);
        LocalDate de = range[0];
        LocalDate ate = range[1];

        List<NotificacaoRow> rows;
        if (de != null && ate != null) {
            rows = repo.findNotificacoesByPeriodo(
                    ent.getId(),
                    Utils.blank(alvara) ? null : alvara,
                    de.atStartOfDay(),
                    ate.atTime(23,59,59)
            );
        } else {
            rows = repo.findNotificacoes(ent.getId(), Utils.blank(alvara) ? null : alvara);
        }
        var data = rows.stream().limit(limit).map(r -> NotificacaoListItemDTO.builder()
                .id(r.getId())
                .assunto(r.getAssunto())
                .dataRegisto(r.getDataRegistro() == null ? null : r.getDataRegistro().toString())
                .meioEnvio(r.getTipo())
                .nProcesso(r.getIdProcesso())
                //.tipoProcesso(processoService.getNomeProcessoByInstanceId(r.getIdProcesso()))
                .ambito(r.getIdPaginaEtapa())
                .nrAlvara(r.getNrAlvara())
                .build()
        ).toList();

        return ApiResponse.ok( "Lista Notificações Carregado com sucesso", data);
    }
}