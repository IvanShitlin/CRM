package ru.shitlin.service.mapper;

import ru.shitlin.domain.Client;
import ru.shitlin.service.dto.ClientDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:09+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client toEntity(ClientDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Client client = new Client();

        client.setCreatedBy( dto.getCreatedBy() );
        client.setCreatedDate( dto.getCreatedDate() );
        client.setLastModifiedBy( dto.getLastModifiedBy() );
        client.setLastModifiedDate( dto.getLastModifiedDate() );
        client.setId( dto.getId() );
        client.setFirstName( dto.getFirstName() );
        client.setLastName( dto.getLastName() );
        client.setPatronymic( dto.getPatronymic() );
        client.setPhone( dto.getPhone() );
        client.setEmail( dto.getEmail() );
        client.setSkype( dto.getSkype() );
        client.setCountry( dto.getCountry() );
        client.setCity( dto.getCity() );
        client.setExperience( dto.getExperience() );
        client.setNote( dto.getNote() );
        client.setAmocrmId( dto.getAmocrmId() );

        return client;
    }

    @Override
    public ClientDTO toDto(Client entity) {
        if ( entity == null ) {
            return null;
        }

        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setCreatedBy( entity.getCreatedBy() );
        clientDTO.setCreatedDate( entity.getCreatedDate() );
        clientDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        clientDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        clientDTO.setId( entity.getId() );
        clientDTO.setFirstName( entity.getFirstName() );
        clientDTO.setLastName( entity.getLastName() );
        clientDTO.setPatronymic( entity.getPatronymic() );
        clientDTO.setPhone( entity.getPhone() );
        clientDTO.setEmail( entity.getEmail() );
        clientDTO.setSkype( entity.getSkype() );
        clientDTO.setCountry( entity.getCountry() );
        clientDTO.setCity( entity.getCity() );
        clientDTO.setExperience( entity.getExperience() );
        clientDTO.setNote( entity.getNote() );
        clientDTO.setAmocrmId( entity.getAmocrmId() );

        return clientDTO;
    }

    @Override
    public List<Client> toEntity(List<ClientDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Client> list = new ArrayList<Client>( dtoList.size() );
        for ( ClientDTO clientDTO : dtoList ) {
            list.add( toEntity( clientDTO ) );
        }

        return list;
    }

    @Override
    public List<ClientDTO> toDto(List<Client> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ClientDTO> list = new ArrayList<ClientDTO>( entityList.size() );
        for ( Client client : entityList ) {
            list.add( toDto( client ) );
        }

        return list;
    }
}
