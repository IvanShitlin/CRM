package ru.shitlin.service.mapper;

import ru.shitlin.domain.CourseType;
import ru.shitlin.service.dto.CourseTypeDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:09+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class CourseTypeMapperImpl implements CourseTypeMapper {

    @Autowired
    private MoneyMapper moneyMapper;

    @Override
    public CourseType toEntity(CourseTypeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CourseType courseType = new CourseType();

        courseType.setId( dto.getId() );
        courseType.setType( dto.getType() );
        courseType.setLocation( dto.getLocation() );
        courseType.setPrice( moneyMapper.toEntity( dto.getPrice() ) );
        courseType.setMentorRate( moneyMapper.toEntity( dto.getMentorRate() ) );

        return courseType;
    }

    @Override
    public CourseTypeDTO toDto(CourseType entity) {
        if ( entity == null ) {
            return null;
        }

        CourseTypeDTO courseTypeDTO = new CourseTypeDTO();

        courseTypeDTO.setId( entity.getId() );
        courseTypeDTO.setType( entity.getType() );
        courseTypeDTO.setLocation( entity.getLocation() );
        courseTypeDTO.setPrice( moneyMapper.toDto( entity.getPrice() ) );
        courseTypeDTO.setMentorRate( moneyMapper.toDto( entity.getMentorRate() ) );

        return courseTypeDTO;
    }

    @Override
    public List<CourseType> toEntity(List<CourseTypeDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<CourseType> list = new ArrayList<CourseType>( dtoList.size() );
        for ( CourseTypeDTO courseTypeDTO : dtoList ) {
            list.add( toEntity( courseTypeDTO ) );
        }

        return list;
    }

    @Override
    public List<CourseTypeDTO> toDto(List<CourseType> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CourseTypeDTO> list = new ArrayList<CourseTypeDTO>( entityList.size() );
        for ( CourseType courseType : entityList ) {
            list.add( toDto( courseType ) );
        }

        return list;
    }
}
