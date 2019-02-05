package com.foxminded.hipsterfox.service.mapper;

import com.foxminded.hipsterfox.domain.Mentor;
import com.foxminded.hipsterfox.service.dto.MentorDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:10+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class MentorMapperImpl implements MentorMapper {

    @Override
    public MentorDTO toDto(Mentor entity) {
        if ( entity == null ) {
            return null;
        }

        MentorDTO mentorDTO = new MentorDTO();

        mentorDTO.setCreatedBy( entity.getCreatedBy() );
        mentorDTO.setCreatedDate( entity.getCreatedDate() );
        mentorDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        mentorDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        mentorDTO.setId( entity.getId() );
        mentorDTO.setFirstName( entity.getFirstName() );
        mentorDTO.setLastName( entity.getLastName() );
        mentorDTO.setPatronymic( entity.getPatronymic() );
        mentorDTO.setPhone( entity.getPhone() );
        mentorDTO.setEmail( entity.getEmail() );
        mentorDTO.setSkype( entity.getSkype() );
        mentorDTO.setCountry( entity.getCountry() );
        mentorDTO.setCity( entity.getCity() );
        mentorDTO.setMaxStudents( entity.getMaxStudents() );

        return mentorDTO;
    }

    @Override
    public List<Mentor> toEntity(List<MentorDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Mentor> list = new ArrayList<Mentor>( dtoList.size() );
        for ( MentorDTO mentorDTO : dtoList ) {
            list.add( toEntity( mentorDTO ) );
        }

        return list;
    }

    @Override
    public List<MentorDTO> toDto(List<Mentor> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MentorDTO> list = new ArrayList<MentorDTO>( entityList.size() );
        for ( Mentor mentor : entityList ) {
            list.add( toDto( mentor ) );
        }

        return list;
    }

    @Override
    public Mentor toEntity(MentorDTO mentorDTO) {
        if ( mentorDTO == null ) {
            return null;
        }

        Mentor mentor = new Mentor();

        mentor.setCreatedBy( mentorDTO.getCreatedBy() );
        mentor.setCreatedDate( mentorDTO.getCreatedDate() );
        mentor.setLastModifiedBy( mentorDTO.getLastModifiedBy() );
        mentor.setLastModifiedDate( mentorDTO.getLastModifiedDate() );
        mentor.setId( mentorDTO.getId() );
        mentor.setFirstName( mentorDTO.getFirstName() );
        mentor.setLastName( mentorDTO.getLastName() );
        mentor.setPatronymic( mentorDTO.getPatronymic() );
        mentor.setPhone( mentorDTO.getPhone() );
        mentor.setEmail( mentorDTO.getEmail() );
        mentor.setSkype( mentorDTO.getSkype() );
        mentor.setCountry( mentorDTO.getCountry() );
        mentor.setCity( mentorDTO.getCity() );
        mentor.setMaxStudents( mentorDTO.getMaxStudents() );

        return mentor;
    }
}
