package cv.gov.dge.paef.infrastructure.mapper;

import cv.gov.dge.paef.application.entidade.dto.EntidadeDTO;
import cv.gov.dge.paef.domain.entidade.model.Entidade;
import cv.gov.dge.paef.infrastructure.EntidadeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T17:28:32-0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class EntidadeMapperImpl implements EntidadeMapper {

    @Override
    public EntidadeEntity toEntity(EntidadeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EntidadeEntity.EntidadeEntityBuilder entidadeEntity = EntidadeEntity.builder();

        entidadeEntity.id( dto.id() );
        entidadeEntity.denominacaoSocial( dto.denominacaoSocial() );
        entidadeEntity.denominacaoComercial( dto.denominacaoComercial() );
        if ( dto.nif() != null ) {
            entidadeEntity.nif( BigDecimal.valueOf( dto.nif() ) );
        }
        entidadeEntity.dmNatureza( dto.dmNatureza() );
        entidadeEntity.dtConstituicao( dto.dtConstituicao() );
        entidadeEntity.nrMatricula( dto.nrMatricula() );
        entidadeEntity.endereco( dto.endereco() );
        entidadeEntity.caixaPostal( dto.caixaPostal() );
        entidadeEntity.telefone( dto.telefone() );
        entidadeEntity.telemovel( dto.telemovel() );
        entidadeEntity.email( dto.email() );
        entidadeEntity.url( dto.url() );
        entidadeEntity.representanteLegal( dto.representanteLegal() );
        entidadeEntity.dmOrigemReg( dto.dmOrigemReg() );
        entidadeEntity.dataRegisto( dto.dataRegisto() );
        if ( dto.userRegistoId() != null ) {
            entidadeEntity.userRegistoId( BigDecimal.valueOf( dto.userRegistoId() ) );
        }
        entidadeEntity.abreviatura( dto.abreviatura() );
        entidadeEntity.geogLocalId( dto.geogLocalId() );
        entidadeEntity.denominacaoSocialNorm( dto.denominacaoSocialNorm() );
        entidadeEntity.isOnPortal( dto.isOnPortal() );

        return entidadeEntity.build();
    }

    @Override
    public Entidade toModel(EntidadeEntity e) {
        if ( e == null ) {
            return null;
        }

        Entidade.EntidadeBuilder entidade = Entidade.builder();

        entidade.id( e.getId() );
        entidade.denominacaoSocial( e.getDenominacaoSocial() );
        entidade.denominacaoComercial( e.getDenominacaoComercial() );
        entidade.nif( e.getNif() );
        entidade.dmNatureza( e.getDmNatureza() );
        entidade.dtConstituicao( e.getDtConstituicao() );
        entidade.nrMatricula( e.getNrMatricula() );
        entidade.endereco( e.getEndereco() );
        entidade.caixaPostal( e.getCaixaPostal() );
        entidade.telefone( e.getTelefone() );
        entidade.telemovel( e.getTelemovel() );
        entidade.email( e.getEmail() );
        entidade.url( e.getUrl() );
        entidade.representanteLegal( e.getRepresentanteLegal() );
        entidade.dmOrigemReg( e.getDmOrigemReg() );
        entidade.dataRegisto( e.getDataRegisto() );
        if ( e.getUserRegistoId() != null ) {
            entidade.userRegistoId( e.getUserRegistoId().longValue() );
        }
        entidade.abreviatura( e.getAbreviatura() );
        entidade.geogLocalId( e.getGeogLocalId() );
        entidade.denominacaoSocialNorm( e.getDenominacaoSocialNorm() );
        entidade.isOnPortal( e.getIsOnPortal() );

        return entidade.build();
    }

    @Override
    public EntidadeEntity toEntity(Entidade model) {
        if ( model == null ) {
            return null;
        }

        EntidadeEntity.EntidadeEntityBuilder entidadeEntity = EntidadeEntity.builder();

        entidadeEntity.id( model.getId() );
        entidadeEntity.denominacaoSocial( model.getDenominacaoSocial() );
        entidadeEntity.denominacaoComercial( model.getDenominacaoComercial() );
        entidadeEntity.nif( model.getNif() );
        entidadeEntity.dmNatureza( model.getDmNatureza() );
        entidadeEntity.dtConstituicao( model.getDtConstituicao() );
        entidadeEntity.nrMatricula( model.getNrMatricula() );
        entidadeEntity.endereco( model.getEndereco() );
        entidadeEntity.caixaPostal( model.getCaixaPostal() );
        entidadeEntity.telefone( model.getTelefone() );
        entidadeEntity.telemovel( model.getTelemovel() );
        entidadeEntity.email( model.getEmail() );
        entidadeEntity.url( model.getUrl() );
        entidadeEntity.representanteLegal( model.getRepresentanteLegal() );
        entidadeEntity.dmOrigemReg( model.getDmOrigemReg() );
        entidadeEntity.dataRegisto( model.getDataRegisto() );
        if ( model.getUserRegistoId() != null ) {
            entidadeEntity.userRegistoId( BigDecimal.valueOf( model.getUserRegistoId() ) );
        }
        entidadeEntity.abreviatura( model.getAbreviatura() );
        entidadeEntity.geogLocalId( model.getGeogLocalId() );
        entidadeEntity.denominacaoSocialNorm( model.getDenominacaoSocialNorm() );
        entidadeEntity.isOnPortal( model.getIsOnPortal() );

        return entidadeEntity.build();
    }

    @Override
    public EntidadeDTO toDto(EntidadeEntity e) {
        if ( e == null ) {
            return null;
        }

        String id = null;
        String denominacaoSocial = null;
        String denominacaoComercial = null;
        Long nif = null;
        String dmNatureza = null;
        LocalDate dtConstituicao = null;
        String nrMatricula = null;
        String endereco = null;
        String caixaPostal = null;
        String telefone = null;
        String telemovel = null;
        String email = null;
        String url = null;
        String representanteLegal = null;
        String dmOrigemReg = null;
        LocalDate dataRegisto = null;
        Long userRegistoId = null;
        String abreviatura = null;
        String geogLocalId = null;
        String denominacaoSocialNorm = null;
        Boolean isOnPortal = null;

        id = e.getId();
        denominacaoSocial = e.getDenominacaoSocial();
        denominacaoComercial = e.getDenominacaoComercial();
        if ( e.getNif() != null ) {
            nif = e.getNif().longValue();
        }
        dmNatureza = e.getDmNatureza();
        dtConstituicao = e.getDtConstituicao();
        nrMatricula = e.getNrMatricula();
        endereco = e.getEndereco();
        caixaPostal = e.getCaixaPostal();
        telefone = e.getTelefone();
        telemovel = e.getTelemovel();
        email = e.getEmail();
        url = e.getUrl();
        representanteLegal = e.getRepresentanteLegal();
        dmOrigemReg = e.getDmOrigemReg();
        dataRegisto = e.getDataRegisto();
        if ( e.getUserRegistoId() != null ) {
            userRegistoId = e.getUserRegistoId().longValue();
        }
        abreviatura = e.getAbreviatura();
        geogLocalId = e.getGeogLocalId();
        denominacaoSocialNorm = e.getDenominacaoSocialNorm();
        isOnPortal = e.getIsOnPortal();

        EntidadeDTO entidadeDTO = new EntidadeDTO( id, denominacaoSocial, denominacaoComercial, nif, dmNatureza, dtConstituicao, nrMatricula, endereco, caixaPostal, telefone, telemovel, email, url, representanteLegal, dmOrigemReg, dataRegisto, userRegistoId, abreviatura, geogLocalId, denominacaoSocialNorm, isOnPortal );

        return entidadeDTO;
    }
}
