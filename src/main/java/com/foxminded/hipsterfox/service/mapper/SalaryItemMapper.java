package com.foxminded.hipsterfox.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import com.foxminded.hipsterfox.service.dto.SalaryItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SalaryItem and its DTO SalaryItemDTO.
 */
@Mapper(componentModel = "spring", uses = {MentorMapper.class, InvoiceMapper.class, SalaryMapper.class})
public interface SalaryItemMapper extends EntityMapper<SalaryItemDTO, SalaryItem> {

    @Mappings({
        @Mapping(source = "mentor.id", target = "mentorId"),
        @Mapping(source = "mentor.firstName", target = "mentorFirstName"),
        @Mapping(source = "mentor.lastName", target = "mentorLastName"),
        @Mapping(source = "invoice.id", target = "invoiceId"),
        @Mapping(source = "salary.id", target = "salaryId"),
    })
    SalaryItemDTO toDto(SalaryItem salaryItem);

    @Mappings({
        @Mapping(source = "mentorId", target = "mentor"),
        @Mapping(source = "invoiceId", target = "invoice"),
        @Mapping(source = "salaryId", target = "salary"),
    })
    SalaryItem toEntity(SalaryItemDTO salaryItemDTO);

    default SalaryItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        SalaryItem salaryItem = new SalaryItem();
        salaryItem.setId(id);
        return salaryItem;
    }
}
