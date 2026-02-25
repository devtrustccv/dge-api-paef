package cv.gov.dge.paef.application.alvara.service;

import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import cv.gov.dge.paef.infrastructure.repository.AlvaraRepository;
import cv.gov.dge.paef.interfaces.dto.Alvara.AlvaraListItemDTO;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

@Service @Transactional
public class AlvaraService {

    private static final String SITUACAO_VALIDO = "A";
    private static final String SIM = "SIM";
    private static final String ORIGEM_RECUP = "RECUP";     // na VIEW: dm_origem_reg usa ONLINE/BACK/RECUP
    private static final String ESTADO_CADUCADO = "CD";     // na VIEW: A, CD, CANC, ...
    private static final String ESTADO_CADUCVALIDO = "CADUCVALIDO";

    private final AlvaraRepository repo;
    public AlvaraService(AlvaraRepository repo){ this.repo = repo; }
    public AlvaraEntity save(AlvaraEntity e){ return repo.save(e); }
    public List<OptionDTO> listOptionsByEntidadeNif(Long nif) {
        var rows = repo.findAlvarasOptionsByNif(new BigDecimal(nif));
        return rows.stream()
                .map(r -> OptionDTO.builder()
                        .key(r.getKey())
                        .value(r.getValue())
                        .build())
                .toList();
    }

    public List<AlvaraListItemDTO> listValidosByNif(Long nif, Integer limit) {

        int lim = (limit == null || limit <= 0) ? 200 : Math.min(limit, 1000);

        var rows = repo.findValidosByNif(new BigDecimal(nif), SITUACAO_VALIDO, lim);
        var sdf = new SimpleDateFormat("yyyy-MM-dd");

        return rows.stream().map(v -> {

            // estadoCod (regra do legado)
            String estadoCod = v.getDmEstadoAlvara();
            if (ESTADO_CADUCADO.equalsIgnoreCase(estadoCod) && SITUACAO_VALIDO.equalsIgnoreCase(v.getDmSituacao())) {
                estadoCod = ESTADO_CADUCVALIDO;
            }

            // visibilidade (equivalente ao hiddenButton)
            boolean canViewAlvara = true;
            boolean canViewAnexo = true;

            if (v.getFlagPublicacao() != null && !SIM.equalsIgnoreCase(v.getFlagPublicacao())) {
                canViewAlvara = false;
                canViewAnexo = false;
            } else if (v.getFlagPublicacao() == null && ORIGEM_RECUP.equalsIgnoreCase(v.getDmOrigemReg())) {
                // no legado: se existir documento "Alvará", esconde anexo_formacoes
                if (Boolean.TRUE.equals(v.getDocAlvaraExists())) {
                    canViewAnexo = false;
                }
            }

            return AlvaraListItemDTO.builder()
                    .num(v.getNrAlvara())
                    .estabelecimento(v.getDesignacao())
                    .tipoAlvara(v.getDescricaoTipoAlvara())
                    .localizacao(v.getLocalizacao()) // se quiseres nome da zona: fazemos join geografia
                    .dtEmissao(v.getDataEmissao() == null ? null : sdf.format(v.getDataEmissao()))
                    .dtValidade(v.getDataValidade() == null ? null : sdf.format(v.getDataValidade()))
                    .estado(v.getEstadoAlvaraDescricao()) // a VIEW já traz a descrição!
                    .situacao(v.getDmSituacao())
                    .idAlvara(v.getIdAlvara())
                    .idTask(v.getIdProcesso())
                    .idEntidade(v.getIdEntidade())
                    .estadoCod(estadoCod)
                    .canViewAlvara(canViewAlvara)
                    .canViewAnexoFormacoes(canViewAnexo)
                    .build();

        }).toList();
    }
}
