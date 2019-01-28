package ru.shitlin.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import ru.shitlin.service.dto.SalaryDTO;

import org.mapstruct.*;
import ru.shitlin.domain.Salary;

/**
 * Mapper for the entity Salary and its DTO SalaryDTO.
 */
@Mapper(componentModel = "spring", uses = {MentorMapper.class, SalaryItemMapper.class})
public interface SalaryMapper extends EntityMapper<SalaryDTO, Salary> {

    @Mappings({
        @Mapping(source = "mentor.id", target = "mentorId"),
        @Mapping(source = "mentor.firstName", target = "mentorFirstName"),
        @Mapping(source = "mentor.lastName", target = "mentorLastName"),
    })
    SalaryDTO toDto(Salary salary);

    @Mappings({
        @Mapping(source = "mentorId", target = "mentor"),
    })
    Salary toEntity(SalaryDTO salaryDTO);

    default Salary fromId(Long id) {
        if (id == null) {
            return null;
        }
        Salary salary = new Salary();
        salary.setId(id);
        return salary;
    }
}
