package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.domain.areaqualif.model.AreaQualif;
import cv.gov.dge.paef.infrastructure.AreaQualifEntity;
import cv.gov.dge.paef.application.AreaQualif.dto.AreaQualifDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AreaQualifMapper {

    AreaQualifEntity toEntity(AreaQualif model);

    AreaQualif toModel(AreaQualifEntity entity);
    @Mapping(target = "siglaCodigo",    source = "codigoQualif")
    @Mapping(target = "idPai",          source = "codigoFamilia")
    @Mapping(target = "dmNivelArabico", source = "nivel")
    @Mapping(target = "descricao",      source = "denominacaoQualif")
    @Mapping(target = "versao",         source = "versao")
    @Mapping(target = "selfIdCnq",      expression = "java(dto.selfId()==null?null:String.valueOf(dto.selfId()))")
    @Mapping(target = "estado",         constant = "A")
    AreaQualif toModel(AreaQualifDTO dto);

    AreaQualifDTO toDto(AreaQualif dto);
}
