package ru.shitlin.service.view.mapper;

import ru.shitlin.domain.Mentor;
import ru.shitlin.domain.view.MentorStudentsView;
import ru.shitlin.service.view.dto.MentorViewDTO;
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
public class MentorViewMapperImpl implements MentorViewMapper {

    @Override
    public MentorStudentsView toEntity(MentorViewDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MentorStudentsView mentorStudentsView = new MentorStudentsView();

        mentorStudentsView.setId( dto.getId() );
        mentorStudentsView.setActiveStudents( dto.getActiveStudents() );

        return mentorStudentsView;
    }

    @Override
    public List<MentorStudentsView> toEntity(List<MentorViewDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<MentorStudentsView> list = new ArrayList<MentorStudentsView>( dtoList.size() );
        for ( MentorViewDTO mentorViewDTO : dtoList ) {
            list.add( toEntity( mentorViewDTO ) );
        }

        return list;
    }

    @Override
    public List<MentorViewDTO> toDto(List<MentorStudentsView> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MentorViewDTO> list = new ArrayList<MentorViewDTO>( entityList.size() );
        for ( MentorStudentsView mentorStudentsView : entityList ) {
            list.add( toDto( mentorStudentsView ) );
        }

        return list;
    }

    @Override
    public MentorViewDTO toDto(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }

        MentorViewDTO mentorViewDTO = new MentorViewDTO();

        String firstName = mentorStudentsViewMentorFirstName( mentorStudentsView );
        if ( firstName != null ) {
            mentorViewDTO.setFirstName( firstName );
        }
        String lastName = mentorStudentsViewMentorLastName( mentorStudentsView );
        if ( lastName != null ) {
            mentorViewDTO.setLastName( lastName );
        }
        String skype = mentorStudentsViewMentorSkype( mentorStudentsView );
        if ( skype != null ) {
            mentorViewDTO.setSkype( skype );
        }
        String country = mentorStudentsViewMentorCountry( mentorStudentsView );
        if ( country != null ) {
            mentorViewDTO.setCountry( country );
        }
        String patronymic = mentorStudentsViewMentorPatronymic( mentorStudentsView );
        if ( patronymic != null ) {
            mentorViewDTO.setPatronymic( patronymic );
        }
        String phone = mentorStudentsViewMentorPhone( mentorStudentsView );
        if ( phone != null ) {
            mentorViewDTO.setPhone( phone );
        }
        String city = mentorStudentsViewMentorCity( mentorStudentsView );
        if ( city != null ) {
            mentorViewDTO.setCity( city );
        }
        Long maxStudents = mentorStudentsViewMentorMaxStudents( mentorStudentsView );
        if ( maxStudents != null ) {
            mentorViewDTO.setMaxStudents( maxStudents );
        }
        Long id = mentorStudentsViewMentorId( mentorStudentsView );
        if ( id != null ) {
            mentorViewDTO.setId( id );
        }
        String email = mentorStudentsViewMentorEmail( mentorStudentsView );
        if ( email != null ) {
            mentorViewDTO.setEmail( email );
        }
        mentorViewDTO.setActiveStudents( mentorStudentsView.getActiveStudents() );

        return mentorViewDTO;
    }

    private String mentorStudentsViewMentorFirstName(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String firstName = mentor.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String mentorStudentsViewMentorLastName(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String lastName = mentor.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String mentorStudentsViewMentorSkype(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String skype = mentor.getSkype();
        if ( skype == null ) {
            return null;
        }
        return skype;
    }

    private String mentorStudentsViewMentorCountry(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String country = mentor.getCountry();
        if ( country == null ) {
            return null;
        }
        return country;
    }

    private String mentorStudentsViewMentorPatronymic(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String patronymic = mentor.getPatronymic();
        if ( patronymic == null ) {
            return null;
        }
        return patronymic;
    }

    private String mentorStudentsViewMentorPhone(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String phone = mentor.getPhone();
        if ( phone == null ) {
            return null;
        }
        return phone;
    }

    private String mentorStudentsViewMentorCity(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String city = mentor.getCity();
        if ( city == null ) {
            return null;
        }
        return city;
    }

    private Long mentorStudentsViewMentorMaxStudents(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        Long maxStudents = mentor.getMaxStudents();
        if ( maxStudents == null ) {
            return null;
        }
        return maxStudents;
    }

    private Long mentorStudentsViewMentorId(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        Long id = mentor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String mentorStudentsViewMentorEmail(MentorStudentsView mentorStudentsView) {
        if ( mentorStudentsView == null ) {
            return null;
        }
        Mentor mentor = mentorStudentsView.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String email = mentor.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }
}
