package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.domain.formacao.model.Formacao;
import cv.gov.dge.paef.infrastructure.FormacaoEntity;
import cv.gov.dge.paef.application.formacao.dto.FormacaoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FormacaoMapper {
    FormacaoEntity toEntity(FormacaoDTO dto);
    Formacao toModel(FormacaoEntity e);
    FormacaoEntity toEntity(Formacao model);
    FormacaoDTO toDto(FormacaoEntity e);
}
