package ru.shitlin.service.mapper;

import ru.shitlin.domain.Course;
import ru.shitlin.service.dto.CourseDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:09+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public Course toEntity(CourseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Course course = new Course();

        course.setCreatedBy( dto.getCreatedBy() );
        course.setCreatedDate( dto.getCreatedDate() );
        course.setLastModifiedBy( dto.getLastModifiedBy() );
        course.setLastModifiedDate( dto.getLastModifiedDate() );
        course.setId( dto.getId() );
        course.setName( dto.getName() );
        byte[] image = dto.getImage();
        if ( image != null ) {
            course.setImage( Arrays.copyOf( image, image.length ) );
        }
        course.setImageContentType( dto.getImageContentType() );
        course.setDescription( dto.getDescription() );
        course.setEmailTemplate( dto.getEmailTemplate() );

        return course;
    }

    @Override
    public CourseDTO toDto(Course entity) {
        if ( entity == null ) {
            return null;
        }

        CourseDTO courseDTO = new CourseDTO();

        courseDTO.setCreatedBy( entity.getCreatedBy() );
        courseDTO.setCreatedDate( entity.getCreatedDate() );
        courseDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        courseDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        courseDTO.setId( entity.getId() );
        courseDTO.setName( entity.getName() );
        byte[] image = entity.getImage();
        if ( image != null ) {
            courseDTO.setImage( Arrays.copyOf( image, image.length ) );
        }
        courseDTO.setImageContentType( entity.getImageContentType() );
        courseDTO.setDescription( entity.getDescription() );
        courseDTO.setEmailTemplate( entity.getEmailTemplate() );

        return courseDTO;
    }

    @Override
    public List<Course> toEntity(List<CourseDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Course> list = new ArrayList<Course>( dtoList.size() );
        for ( CourseDTO courseDTO : dtoList ) {
            list.add( toEntity( courseDTO ) );
        }

        return list;
    }

    @Override
    public List<CourseDTO> toDto(List<Course> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CourseDTO> list = new ArrayList<CourseDTO>( entityList.size() );
        for ( Course course : entityList ) {
            list.add( toDto( course ) );
        }

        return list;
    }
}
