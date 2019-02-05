package com.foxminded.hipsterfox.service.mapper;

import com.foxminded.hipsterfox.domain.Course;
import com.foxminded.hipsterfox.domain.Money;
import com.foxminded.hipsterfox.service.dto.CourseDTO;
import com.foxminded.hipsterfox.service.dto.MoneyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper for the entity Money and its DTO MoneyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MoneyMapper extends EntityMapper<MoneyDto, Money> {

    MoneyDto toDto(Money money);

    Money toEntity(MoneyDto moneyDto);

}
