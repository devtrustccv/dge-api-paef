package cv.gov.dge.paef.interfaces.dto.Alvara;

import cv.gov.dge.paef.interfaces.dto.EquipamentoDTO;
import cv.gov.dge.paef.interfaces.dto.OptionDTO;
import cv.gov.dge.paef.interfaces.dto.Qualificacao.FormacaoDTO;
import lombok.Builder;
import java.util.List;

@Builder
public record AlvaraDetalheDTO(
        // Alvará
        String idAlvara,
        String idEntidade,
        String idTask,
        String nrAlvara,
        String tipoAlvara,
        String situacao,
        String dataEmissao,
        String dataPedido,
        String dataValidade,
        String estado,
        String tipoProcesso,
        String nrProcesso,

        // Estabelecimento
        String designacao,
        String ilha,
        String concelho,
        String freguesia,
        String zona,
        String concelhoId,
        String freguesiaId,
        String zonaId,
        String endereco,
        String caixaPostal,
        String email,
        String telefone,
        String telemovel,

        // Responsável
        String nomeResp,
        String tipoDocumentoResp,
        String nrDocumentoResp,
        String telemovelResp,
        String telefoneResp,
        String emailResp,
        String enderecoResp,
        String nivelFormacaoAcademica,

        // Listas
        List<FormacaoDTO> formacoes,
        List<EquipamentoDTO> equipamentos,
        List<OptionDTO> recursosHumanos
        //,
        //List<HistoricoAlvaraDTO> historico
) {}
