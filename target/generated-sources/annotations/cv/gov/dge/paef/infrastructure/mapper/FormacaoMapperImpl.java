package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.application.formacao.dto.FormacaoDTO;
import cv.gov.dge.paef.domain.formacao.model.Formacao;
import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T17:28:32-0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class FormacaoMapperImpl implements FormacaoMapper {

    @Override
    public FormacaoEntity toEntity(FormacaoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        FormacaoEntity.FormacaoEntityBuilder formacaoEntity = FormacaoEntity.builder();

        formacaoEntity.id( dto.id() );
        formacaoEntity.idAlvara( dto.idAlvara() );
        formacaoEntity.dmTipoFormacao( dto.dmTipoFormacao() );
        formacaoEntity.idAreaQualif( dto.idAreaQualif() );
        formacaoEntity.idQualificacao( dto.idQualificacao() );
        formacaoEntity.areaDesc( dto.areaDesc() );
        formacaoEntity.qualificacaoDesc( dto.qualificacaoDesc() );
        formacaoEntity.dmNivelRomano( dto.dmNivelRomano() );
        formacaoEntity.flagCatalogo( dto.flagCatalogo() );
        formacaoEntity.dtRegisto( dto.dtRegisto() );
        formacaoEntity.dtFimPrev( dto.dtFimPrev() );
        formacaoEntity.dtIniPrev( dto.dtIniPrev() );
        formacaoEntity.dmMetodologia( dto.dmMetodologia() );
        formacaoEntity.selfIdCnq( dto.selfIdCnq() );
        formacaoEntity.dmEstado( dto.dmEstado() );
        formacaoEntity.versao( dto.versao() );

        return formacaoEntity.build();
    }

    @Override
    public Formacao toModel(FormacaoEntity e) {
        if ( e == null ) {
            return null;
        }

        Formacao.FormacaoBuilder formacao = Formacao.builder();

        formacao.id( e.getId() );
        formacao.idAlvara( e.getIdAlvara() );
        formacao.dmTipoFormacao( e.getDmTipoFormacao() );
        formacao.idAreaQualif( e.getIdAreaQualif() );
        formacao.idQualificacao( e.getIdQualificacao() );
        formacao.areaDesc( e.getAreaDesc() );
        formacao.qualificacaoDesc( e.getQualificacaoDesc() );
        formacao.dmNivelRomano( e.getDmNivelRomano() );
        formacao.flagCatalogo( e.getFlagCatalogo() );
        formacao.dtRegisto( e.getDtRegisto() );
        formacao.dtFimPrev( e.getDtFimPrev() );
        formacao.dtIniPrev( e.getDtIniPrev() );
        formacao.dmMetodologia( e.getDmMetodologia() );
        formacao.selfIdCnq( e.getSelfIdCnq() );
        formacao.versao( e.getVersao() );
        formacao.dmEstado( e.getDmEstado() );

        return formacao.build();
    }

    @Override
    public FormacaoEntity toEntity(Formacao model) {
        if ( model == null ) {
            return null;
        }

        FormacaoEntity.FormacaoEntityBuilder formacaoEntity = FormacaoEntity.builder();

        formacaoEntity.id( model.getId() );
        formacaoEntity.idAlvara( model.getIdAlvara() );
        formacaoEntity.dmTipoFormacao( model.getDmTipoFormacao() );
        formacaoEntity.idAreaQualif( model.getIdAreaQualif() );
        formacaoEntity.idQualificacao( model.getIdQualificacao() );
        formacaoEntity.areaDesc( model.getAreaDesc() );
        formacaoEntity.qualificacaoDesc( model.getQualificacaoDesc() );
        formacaoEntity.dmNivelRomano( model.getDmNivelRomano() );
        formacaoEntity.flagCatalogo( model.getFlagCatalogo() );
        formacaoEntity.dtRegisto( model.getDtRegisto() );
        formacaoEntity.dtFimPrev( model.getDtFimPrev() );
        formacaoEntity.dtIniPrev( model.getDtIniPrev() );
        formacaoEntity.dmMetodologia( model.getDmMetodologia() );
        formacaoEntity.selfIdCnq( model.getSelfIdCnq() );
        formacaoEntity.dmEstado( model.getDmEstado() );
        formacaoEntity.versao( model.getVersao() );

        return formacaoEntity.build();
    }

    @Override
    public FormacaoDTO toDto(FormacaoEntity e) {
        if ( e == null ) {
            return null;
        }

        String id = null;
        String idAlvara = null;
        String dmTipoFormacao = null;
        String idAreaQualif = null;
        String idQualificacao = null;
        String areaDesc = null;
        String qualificacaoDesc = null;
        String dmNivelRomano = null;
        String flagCatalogo = null;
        LocalDate dtRegisto = null;
        LocalDate dtFimPrev = null;
        LocalDate dtIniPrev = null;
        String dmMetodologia = null;
        String selfIdCnq = null;
        String versao = null;
        String dmEstado = null;

        id = e.getId();
        idAlvara = e.getIdAlvara();
        dmTipoFormacao = e.getDmTipoFormacao();
        idAreaQualif = e.getIdAreaQualif();
        idQualificacao = e.getIdQualificacao();
        areaDesc = e.getAreaDesc();
        qualificacaoDesc = e.getQualificacaoDesc();
        dmNivelRomano = e.getDmNivelRomano();
        flagCatalogo = e.getFlagCatalogo();
        dtRegisto = e.getDtRegisto();
        dtFimPrev = e.getDtFimPrev();
        dtIniPrev = e.getDtIniPrev();
        dmMetodologia = e.getDmMetodologia();
        selfIdCnq = e.getSelfIdCnq();
        versao = e.getVersao();
        dmEstado = e.getDmEstado();

        FormacaoDTO formacaoDTO = new FormacaoDTO( id, idAlvara, dmTipoFormacao, idAreaQualif, idQualificacao, areaDesc, qualificacaoDesc, dmNivelRomano, flagCatalogo, dtRegisto, dtFimPrev, dtIniPrev, dmMetodologia, selfIdCnq, versao, dmEstado );

        return formacaoDTO;
    }
}
