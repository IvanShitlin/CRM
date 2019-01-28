package ru.shitlin.service.mapper;

import ru.shitlin.domain.Money;
import ru.shitlin.service.dto.MoneyDto;
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
