package ru.shitlin.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import ru.shitlin.service.dto.MentorDTO;

import org.mapstruct.*;
import ru.shitlin.domain.Mentor;

/**
 * Mapper for the entity Mentor and its DTO MentorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MentorMapper extends EntityMapper<MentorDTO, Mentor> {


    @Mapping(target = "courses", ignore = true)
    Mentor toEntity(MentorDTO mentorDTO);

    default Mentor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Mentor mentor = new Mentor();
        mentor.setId(id);
        return mentor;
    }
}
