package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T17:28:32-0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class AreaQualifMapperImpl implements AreaQualifMapper {

    @Override
    public AreaQualifEntity toEntity(AreaQualif model) {
        if ( model == null ) {
            return null;
        }

        AreaQualifEntity.AreaQualifEntityBuilder areaQualifEntity = AreaQualifEntity.builder();

        areaQualifEntity.id( model.getId() );
        areaQualifEntity.idPai( model.getIdPai() );
        areaQualifEntity.siglaCodigo( model.getSiglaCodigo() );
        areaQualifEntity.dmNivelArabico( model.getDmNivelArabico() );
        areaQualifEntity.descricao( model.getDescricao() );
        areaQualifEntity.estado( model.getEstado() );
        areaQualifEntity.selfIdCnq( model.getSelfIdCnq() );
        areaQualifEntity.versao( model.getVersao() );

        return areaQualifEntity.build();
    }

    @Override
    public AreaQualif toModel(AreaQualifEntity entity) {
        if ( entity == null ) {
            return null;
        }

        AreaQualif.AreaQualifBuilder areaQualif = AreaQualif.builder();

        areaQualif.id( entity.getId() );
        areaQualif.idPai( entity.getIdPai() );
        areaQualif.siglaCodigo( entity.getSiglaCodigo() );
        areaQualif.dmNivelArabico( entity.getDmNivelArabico() );
        areaQualif.descricao( entity.getDescricao() );
        areaQualif.estado( entity.getEstado() );
        areaQualif.selfIdCnq( entity.getSelfIdCnq() );
        areaQualif.versao( entity.getVersao() );

        return areaQualif.build();
    }

    @Override
    public AreaQualif toModel(AreaQualifDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AreaQualif.AreaQualifBuilder areaQualif = AreaQualif.builder();

        areaQualif.siglaCodigo( dto.codigoQualif() );
        areaQualif.dmNivelArabico( dto.nivel() );
        areaQualif.descricao( dto.denominacaoQualif() );
        areaQualif.versao( dto.versao() );

        areaQualif.selfIdCnq( dto.selfId()==null?null:String.valueOf(dto.selfId()) );
        areaQualif.estado( "A" );

        return areaQualif.build();
    }

    @Override
    public AreaQualifDTO toDto(AreaQualif dto) {
        if ( dto == null ) {
            return null;
        }

        String versao = null;

        versao = dto.getVersao();

        String codigoQualif = null;
        String selfId = null;
        String denominacaoQualif = null;
        String nivel = null;
        String codigoFamilia = null;
        String denominacaoFamilia = null;
        String tipo = null;

        AreaQualifDTO areaQualifDTO = new AreaQualifDTO( codigoQualif, selfId, denominacaoQualif, versao, nivel, codigoFamilia, denominacaoFamilia, tipo );

        return areaQualifDTO;
    }
}
