package ru.shitlin.service.mapper;

import ru.shitlin.domain.CourseType;
import ru.shitlin.service.dto.CourseTypeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity CourseType and its DTO CourseTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {MoneyMapper.class})
public interface CourseTypeMapper extends EntityMapper<CourseTypeDTO, CourseType> {

    default CourseType fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseType courseType = new CourseType();
        courseType.setId(id);
        return courseType;
    }
}
