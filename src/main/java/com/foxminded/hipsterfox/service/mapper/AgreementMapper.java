package com.foxminded.hipsterfox.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import com.foxminded.hipsterfox.service.dto.AgreementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Agreement and its DTO AgreementDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, CourseMapper.class})
public interface AgreementMapper extends EntityMapper<AgreementDTO, Agreement> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "courseName")
    @Mapping(source = "client.lastName", target = "clientLastName")
    @Mapping(source = "client.firstName", target = "clientFirstName")
    AgreementDTO toDto(Agreement agreement);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "courseId", target = "course")
    @Mapping(target = "status", ignore = true)
    Agreement toEntity(AgreementDTO agreementDTO);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "courseId", target = "course")
    @Mapping(target = "status", ignore = true)
    Agreement updateAgreementFromDto(AgreementDTO agreementDTO, @MappingTarget Agreement agreement);

    default Agreement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Agreement agreement = new Agreement();
        agreement.setId(id);
        return agreement;
    }
}
