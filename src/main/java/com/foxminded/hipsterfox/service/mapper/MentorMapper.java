package com.foxminded.hipsterfox.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import com.foxminded.hipsterfox.service.dto.MentorDTO;

import org.mapstruct.*;

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
