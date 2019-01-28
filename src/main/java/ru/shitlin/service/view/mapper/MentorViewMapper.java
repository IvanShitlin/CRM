package ru.shitlin.service.view.mapper;

import ru.shitlin.domain.view.MentorStudentsView;
import ru.shitlin.service.mapper.EntityMapper;
import ru.shitlin.service.view.dto.MentorViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper for the entity MentorStudentsView and its DTO MentorViewDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MentorViewMapper extends EntityMapper<MentorViewDTO, MentorStudentsView> {

    @Mappings({
        @Mapping(source = "mentor.id", target = "id"),
        @Mapping(source = "mentor.firstName", target = "firstName"),
        @Mapping(source = "mentor.lastName", target = "lastName"),
        @Mapping(source = "mentor.patronymic", target = "patronymic"),
        @Mapping(source = "mentor.phone", target = "phone"),
        @Mapping(source = "mentor.email", target = "email"),
        @Mapping(source = "mentor.skype", target = "skype"),
        @Mapping(source = "mentor.country", target = "country"),
        @Mapping(source = "mentor.city", target = "city"),
        @Mapping(source = "mentor.maxStudents", target = "maxStudents")
    })
    MentorViewDTO toDto(MentorStudentsView mentorStudentsView);

    default MentorStudentsView fromId(Long id) {
        if (id == null) {
            return null;
        }
        MentorStudentsView mentorStudentsView = new MentorStudentsView();
        mentorStudentsView.setId(id);
        return mentorStudentsView;
    }
}

