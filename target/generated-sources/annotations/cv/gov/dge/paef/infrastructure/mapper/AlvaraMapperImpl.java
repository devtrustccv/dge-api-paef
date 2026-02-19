package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.application.alvara.dto.AlvaraDTO;
import cv.gov.dge.paef.domain.alvara.model.Alvara;
import cv.gov.dge.paef.infrastructure.AlvaraEntity;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T17:28:32-0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class AlvaraMapperImpl implements AlvaraMapper {

    @Override
    public AlvaraEntity toEntity(AlvaraDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AlvaraEntity.AlvaraEntityBuilder alvaraEntity = AlvaraEntity.builder();

        alvaraEntity.id( dto.id() );
        alvaraEntity.idEntidade( dto.idEntidade() );
        alvaraEntity.idPedido( dto.idPedido() );
        alvaraEntity.idTipoAlvara( dto.idTipoAlvara() );
        alvaraEntity.idProcesso( dto.idProcesso() );
        alvaraEntity.dmEstadoAlvara( dto.dmEstadoAlvara() );
        alvaraEntity.dataPedido( dto.dataPedido() );
        alvaraEntity.dataEmissao( dto.dataEmissao() );
        alvaraEntity.dataValidade( dto.dataValidade() );
        alvaraEntity.idEstabelecimento( dto.idEstabelecimento() );
        alvaraEntity.nrProcesso( dto.nrProcesso() );
        alvaraEntity.dmTipoValidade( dto.dmTipoValidade() );
        alvaraEntity.dataDespacho( dto.dataDespacho() );
        alvaraEntity.observacao( dto.observacao() );
        alvaraEntity.dmSituacao( dto.dmSituacao() );
        alvaraEntity.parceria( dto.parceria() );
        alvaraEntity.nrAlvara( dto.nrAlvara() );
        alvaraEntity.dmOrigemReg( dto.dmOrigemReg() );
        alvaraEntity.dtEnvIncv( dto.dtEnvIncv() );
        alvaraEntity.referenciaBo( dto.referenciaBo() );
        alvaraEntity.dtPubBo( dto.dtPubBo() );
        alvaraEntity.obsBo( dto.obsBo() );
        alvaraEntity.linkReportAlvara( dto.linkReportAlvara() );
        alvaraEntity.flagPublicacao( dto.flagPublicacao() );
        alvaraEntity.contraProva( dto.contraProva() );

        return alvaraEntity.build();
    }

    @Override
    public Alvara toModel(AlvaraEntity e) {
        if ( e == null ) {
            return null;
        }

        Alvara.AlvaraBuilder alvara = Alvara.builder();

        alvara.id( e.getId() );
        alvara.idEntidade( e.getIdEntidade() );
        alvara.idPedido( e.getIdPedido() );
        alvara.idTipoAlvara( e.getIdTipoAlvara() );
        alvara.idProcesso( e.getIdProcesso() );
        alvara.dmEstadoAlvara( e.getDmEstadoAlvara() );
        alvara.dataPedido( e.getDataPedido() );
        alvara.dataEmissao( e.getDataEmissao() );
        alvara.dataValidade( e.getDataValidade() );
        alvara.idEstabelecimento( e.getIdEstabelecimento() );
        alvara.nrProcesso( e.getNrProcesso() );
        alvara.dmTipoValidade( e.getDmTipoValidade() );
        alvara.dataDespacho( e.getDataDespacho() );
        alvara.observacao( e.getObservacao() );
        alvara.dmSituacao( e.getDmSituacao() );
        alvara.parceria( e.getParceria() );
        alvara.nrAlvara( e.getNrAlvara() );
        alvara.dmOrigemReg( e.getDmOrigemReg() );
        alvara.dtEnvIncv( e.getDtEnvIncv() );
        alvara.referenciaBo( e.getReferenciaBo() );
        alvara.dtPubBo( e.getDtPubBo() );
        alvara.obsBo( e.getObsBo() );
        alvara.linkReportAlvara( e.getLinkReportAlvara() );
        alvara.flagPublicacao( e.getFlagPublicacao() );
        alvara.contraProva( e.getContraProva() );

        return alvara.build();
    }

    @Override
    public AlvaraEntity toEntity(Alvara model) {
        if ( model == null ) {
            return null;
        }

        AlvaraEntity.AlvaraEntityBuilder alvaraEntity = AlvaraEntity.builder();

        alvaraEntity.id( model.getId() );
        alvaraEntity.idEntidade( model.getIdEntidade() );
        alvaraEntity.idPedido( model.getIdPedido() );
        alvaraEntity.idTipoAlvara( model.getIdTipoAlvara() );
        alvaraEntity.idProcesso( model.getIdProcesso() );
        alvaraEntity.dmEstadoAlvara( model.getDmEstadoAlvara() );
        alvaraEntity.dataPedido( model.getDataPedido() );
        alvaraEntity.dataEmissao( model.getDataEmissao() );
        alvaraEntity.dataValidade( model.getDataValidade() );
        alvaraEntity.idEstabelecimento( model.getIdEstabelecimento() );
        alvaraEntity.nrProcesso( model.getNrProcesso() );
        alvaraEntity.dmTipoValidade( model.getDmTipoValidade() );
        alvaraEntity.dataDespacho( model.getDataDespacho() );
        alvaraEntity.observacao( model.getObservacao() );
        alvaraEntity.dmSituacao( model.getDmSituacao() );
        alvaraEntity.parceria( model.getParceria() );
        alvaraEntity.nrAlvara( model.getNrAlvara() );
        alvaraEntity.dmOrigemReg( model.getDmOrigemReg() );
        alvaraEntity.dtEnvIncv( model.getDtEnvIncv() );
        alvaraEntity.referenciaBo( model.getReferenciaBo() );
        alvaraEntity.dtPubBo( model.getDtPubBo() );
        alvaraEntity.obsBo( model.getObsBo() );
        alvaraEntity.linkReportAlvara( model.getLinkReportAlvara() );
        alvaraEntity.flagPublicacao( model.getFlagPublicacao() );
        alvaraEntity.contraProva( model.getContraProva() );

        return alvaraEntity.build();
    }

    @Override
    public AlvaraDTO toDto(AlvaraEntity e) {
        if ( e == null ) {
            return null;
        }

        String id = null;
        String idEntidade = null;
        String idPedido = null;
        String idTipoAlvara = null;
        String idProcesso = null;
        String dmEstadoAlvara = null;
        LocalDate dataPedido = null;
        LocalDate dataEmissao = null;
        LocalDate dataValidade = null;
        String idEstabelecimento = null;
        String nrProcesso = null;
        String dmTipoValidade = null;
        LocalDate dataDespacho = null;
        String observacao = null;
        String dmSituacao = null;
        String parceria = null;
        String nrAlvara = null;
        String dmOrigemReg = null;
        LocalDate dtEnvIncv = null;
        String referenciaBo = null;
        LocalDate dtPubBo = null;
        String obsBo = null;
        String linkReportAlvara = null;
        String flagPublicacao = null;
        String contraProva = null;

        id = e.getId();
        idEntidade = e.getIdEntidade();
        idPedido = e.getIdPedido();
        idTipoAlvara = e.getIdTipoAlvara();
        idProcesso = e.getIdProcesso();
        dmEstadoAlvara = e.getDmEstadoAlvara();
        dataPedido = e.getDataPedido();
        dataEmissao = e.getDataEmissao();
        dataValidade = e.getDataValidade();
        idEstabelecimento = e.getIdEstabelecimento();
        nrProcesso = e.getNrProcesso();
        dmTipoValidade = e.getDmTipoValidade();
        dataDespacho = e.getDataDespacho();
        observacao = e.getObservacao();
        dmSituacao = e.getDmSituacao();
        parceria = e.getParceria();
        nrAlvara = e.getNrAlvara();
        dmOrigemReg = e.getDmOrigemReg();
        dtEnvIncv = e.getDtEnvIncv();
        referenciaBo = e.getReferenciaBo();
        dtPubBo = e.getDtPubBo();
        obsBo = e.getObsBo();
        linkReportAlvara = e.getLinkReportAlvara();
        flagPublicacao = e.getFlagPublicacao();
        contraProva = e.getContraProva();

        AlvaraDTO alvaraDTO = new AlvaraDTO( id, idEntidade, idPedido, idTipoAlvara, idProcesso, dmEstadoAlvara, dataPedido, dataEmissao, dataValidade, idEstabelecimento, nrProcesso, dmTipoValidade, dataDespacho, observacao, dmSituacao, parceria, nrAlvara, dmOrigemReg, dtEnvIncv, referenciaBo, dtPubBo, obsBo, linkReportAlvara, flagPublicacao, contraProva );

        return alvaraDTO;
    }
}
