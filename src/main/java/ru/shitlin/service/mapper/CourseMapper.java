package ru.shitlin.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import ru.shitlin.service.dto.CourseDTO;

import org.mapstruct.*;
import ru.shitlin.domain.Course;
import ru.shitlin.domain.Mentor;

/**
 * Mapper for the entity Course and its DTO CourseDTO.
 */
@Mapper(componentModel = "spring", uses = {Mentor.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {



    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
