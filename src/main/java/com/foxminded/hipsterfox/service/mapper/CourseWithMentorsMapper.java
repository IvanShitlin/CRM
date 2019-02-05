package com.foxminded.hipsterfox.service.mapper;

import com.foxminded.hipsterfox.domain.Course;
import com.foxminded.hipsterfox.service.dto.CourseWithMentorsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseWithMentorsMapper extends EntityMapper<CourseWithMentorsDTO, Course> {
    CourseWithMentorsDTO toDto(Course course);

    Course toEntity(CourseWithMentorsDTO courseWithMentorsDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
