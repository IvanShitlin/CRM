package ru.shitlin.service.mapper;

import ru.shitlin.domain.Money;
import ru.shitlin.service.dto.MoneyDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:09+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class MoneyMapperImpl implements MoneyMapper {

    @Override
    public List<Money> toEntity(List<MoneyDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Money> list = new ArrayList<Money>( dtoList.size() );
        for ( MoneyDto moneyDto : dtoList ) {
            list.add( toEntity( moneyDto ) );
        }

        return list;
    }

    @Override
    public List<MoneyDto> toDto(List<Money> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MoneyDto> list = new ArrayList<MoneyDto>( entityList.size() );
        for ( Money money : entityList ) {
            list.add( toDto( money ) );
        }

        return list;
    }

    @Override
    public MoneyDto toDto(Money money) {
        if ( money == null ) {
            return null;
        }

        MoneyDto moneyDto = new MoneyDto();

        moneyDto.setAmount( money.getAmount() );
        moneyDto.setCurrency( money.getCurrency() );

        return moneyDto;
    }

    @Override
    public Money toEntity(MoneyDto moneyDto) {
        if ( moneyDto == null ) {
            return null;
        }

        Money money = new Money();

        money.setCurrency( moneyDto.getCurrency() );
        money.setAmount( moneyDto.getAmount() );

        return money;
    }
}
