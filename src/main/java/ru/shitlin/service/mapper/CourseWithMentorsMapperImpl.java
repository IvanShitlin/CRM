package ru.shitlin.service.mapper;

import ru.shitlin.domain.Course;
import ru.shitlin.domain.Mentor;
import ru.shitlin.service.dto.CourseWithMentorsDTO;
import ru.shitlin.service.dto.MentorDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:09+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class CourseWithMentorsMapperImpl implements CourseWithMentorsMapper {

    @Override
    public List<Course> toEntity(List<CourseWithMentorsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Course> list = new ArrayList<Course>( dtoList.size() );
        for ( CourseWithMentorsDTO courseWithMentorsDTO : dtoList ) {
            list.add( toEntity( courseWithMentorsDTO ) );
        }

        return list;
    }

    @Override
    public List<CourseWithMentorsDTO> toDto(List<Course> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CourseWithMentorsDTO> list = new ArrayList<CourseWithMentorsDTO>( entityList.size() );
        for ( Course course : entityList ) {
            list.add( toDto( course ) );
        }

        return list;
    }

    @Override
    public CourseWithMentorsDTO toDto(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseWithMentorsDTO courseWithMentorsDTO = new CourseWithMentorsDTO();

        courseWithMentorsDTO.setId( course.getId() );
        courseWithMentorsDTO.setName( course.getName() );
        byte[] image = course.getImage();
        if ( image != null ) {
            courseWithMentorsDTO.setImage( Arrays.copyOf( image, image.length ) );
        }
        courseWithMentorsDTO.setImageContentType( course.getImageContentType() );
        courseWithMentorsDTO.setDescription( course.getDescription() );
        courseWithMentorsDTO.setEmailTemplate( course.getEmailTemplate() );
        courseWithMentorsDTO.setMentors( mentorSetToMentorDTOSet( course.getMentors() ) );

        return courseWithMentorsDTO;
    }

    @Override
    public Course toEntity(CourseWithMentorsDTO courseWithMentorsDTO) {
        if ( courseWithMentorsDTO == null ) {
            return null;
        }

        Course course = new Course();

        course.setId( courseWithMentorsDTO.getId() );
        course.setName( courseWithMentorsDTO.getName() );
        byte[] image = courseWithMentorsDTO.getImage();
        if ( image != null ) {
            course.setImage( Arrays.copyOf( image, image.length ) );
        }
        course.setImageContentType( courseWithMentorsDTO.getImageContentType() );
        course.setDescription( courseWithMentorsDTO.getDescription() );
        course.setEmailTemplate( courseWithMentorsDTO.getEmailTemplate() );
        course.setMentors( mentorDTOSetToMentorSet( courseWithMentorsDTO.getMentors() ) );

        return course;
    }

    protected MentorDTO mentorToMentorDTO(Mentor mentor) {
        if ( mentor == null ) {
            return null;
        }

        MentorDTO mentorDTO = new MentorDTO();

        mentorDTO.setCreatedBy( mentor.getCreatedBy() );
        mentorDTO.setCreatedDate( mentor.getCreatedDate() );
        mentorDTO.setLastModifiedBy( mentor.getLastModifiedBy() );
        mentorDTO.setLastModifiedDate( mentor.getLastModifiedDate() );
        mentorDTO.setId( mentor.getId() );
        mentorDTO.setFirstName( mentor.getFirstName() );
        mentorDTO.setLastName( mentor.getLastName() );
        mentorDTO.setPatronymic( mentor.getPatronymic() );
        mentorDTO.setPhone( mentor.getPhone() );
        mentorDTO.setEmail( mentor.getEmail() );
        mentorDTO.setSkype( mentor.getSkype() );
        mentorDTO.setCountry( mentor.getCountry() );
        mentorDTO.setCity( mentor.getCity() );
        mentorDTO.setMaxStudents( mentor.getMaxStudents() );

        return mentorDTO;
    }

    protected Set<MentorDTO> mentorSetToMentorDTOSet(Set<Mentor> set) {
        if ( set == null ) {
            return null;
        }

        Set<MentorDTO> set1 = new HashSet<MentorDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Mentor mentor : set ) {
            set1.add( mentorToMentorDTO( mentor ) );
        }

        return set1;
    }

    protected Mentor mentorDTOToMentor(MentorDTO mentorDTO) {
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

    protected Set<Mentor> mentorDTOSetToMentorSet(Set<MentorDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Mentor> set1 = new HashSet<Mentor>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( MentorDTO mentorDTO : set ) {
            set1.add( mentorDTOToMentor( mentorDTO ) );
        }

        return set1;
    }
}
