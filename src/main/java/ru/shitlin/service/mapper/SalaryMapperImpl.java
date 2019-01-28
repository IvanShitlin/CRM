package ru.shitlin.service.mapper;

import ru.shitlin.domain.Mentor;
import ru.shitlin.domain.Money;
import ru.shitlin.domain.Salary;
import ru.shitlin.domain.SalaryItem;
import ru.shitlin.service.dto.MoneyDto;
import ru.shitlin.service.dto.SalaryDTO;
import ru.shitlin.service.dto.SalaryItemDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:09+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class SalaryMapperImpl implements SalaryMapper {

    @Autowired
    private MentorMapper mentorMapper;
    @Autowired
    private SalaryItemMapper salaryItemMapper;

    @Override
    public List<Salary> toEntity(List<SalaryDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Salary> list = new ArrayList<Salary>( dtoList.size() );
        for ( SalaryDTO salaryDTO : dtoList ) {
            list.add( toEntity( salaryDTO ) );
        }

        return list;
    }

    @Override
    public List<SalaryDTO> toDto(List<Salary> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SalaryDTO> list = new ArrayList<SalaryDTO>( entityList.size() );
        for ( Salary salary : entityList ) {
            list.add( toDto( salary ) );
        }

        return list;
    }

    @Override
    public SalaryDTO toDto(Salary salary) {
        if ( salary == null ) {
            return null;
        }

        SalaryDTO salaryDTO = new SalaryDTO();

        String lastName = salaryMentorLastName( salary );
        if ( lastName != null ) {
            salaryDTO.setMentorLastName( lastName );
        }
        String firstName = salaryMentorFirstName( salary );
        if ( firstName != null ) {
            salaryDTO.setMentorFirstName( firstName );
        }
        Long id = salaryMentorId( salary );
        if ( id != null ) {
            salaryDTO.setMentorId( id );
        }
        salaryDTO.setCreatedBy( salary.getCreatedBy() );
        salaryDTO.setCreatedDate( salary.getCreatedDate() );
        salaryDTO.setLastModifiedBy( salary.getLastModifiedBy() );
        salaryDTO.setLastModifiedDate( salary.getLastModifiedDate() );
        salaryDTO.setId( salary.getId() );
        salaryDTO.setDateFrom( salary.getDateFrom() );
        salaryDTO.setDateTo( salary.getDateTo() );
        salaryDTO.setPaid( salary.isPaid() );
        salaryDTO.setPaidDate( salary.getPaidDate() );
        salaryDTO.setSum( moneyToMoneyDto( salary.getSum() ) );
        salaryDTO.setItems( salaryItemSetToSalaryItemDTOSet( salary.getItems() ) );

        return salaryDTO;
    }

    @Override
    public Salary toEntity(SalaryDTO salaryDTO) {
        if ( salaryDTO == null ) {
            return null;
        }

        Salary salary = new Salary();

        salary.setMentor( mentorMapper.fromId( salaryDTO.getMentorId() ) );
        salary.setCreatedBy( salaryDTO.getCreatedBy() );
        salary.setCreatedDate( salaryDTO.getCreatedDate() );
        salary.setLastModifiedBy( salaryDTO.getLastModifiedBy() );
        salary.setLastModifiedDate( salaryDTO.getLastModifiedDate() );
        salary.setId( salaryDTO.getId() );
        salary.setDateFrom( salaryDTO.getDateFrom() );
        salary.setDateTo( salaryDTO.getDateTo() );
        salary.setPaid( salaryDTO.isPaid() );
        salary.setPaidDate( salaryDTO.getPaidDate() );
        salary.setItems( salaryItemDTOSetToSalaryItemSet( salaryDTO.getItems() ) );
        salary.setSum( moneyDtoToMoney( salaryDTO.getSum() ) );

        return salary;
    }

    private String salaryMentorLastName(Salary salary) {
        if ( salary == null ) {
            return null;
        }
        Mentor mentor = salary.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String lastName = mentor.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String salaryMentorFirstName(Salary salary) {
        if ( salary == null ) {
            return null;
        }
        Mentor mentor = salary.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String firstName = mentor.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private Long salaryMentorId(Salary salary) {
        if ( salary == null ) {
            return null;
        }
        Mentor mentor = salary.getMentor();
        if ( mentor == null ) {
            return null;
        }
        Long id = mentor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected MoneyDto moneyToMoneyDto(Money money) {
        if ( money == null ) {
            return null;
        }

        MoneyDto moneyDto = new MoneyDto();

        moneyDto.setAmount( money.getAmount() );
        moneyDto.setCurrency( money.getCurrency() );

        return moneyDto;
    }

    protected Set<SalaryItemDTO> salaryItemSetToSalaryItemDTOSet(Set<SalaryItem> set) {
        if ( set == null ) {
            return null;
        }

        Set<SalaryItemDTO> set1 = new HashSet<SalaryItemDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( SalaryItem salaryItem : set ) {
            set1.add( salaryItemMapper.toDto( salaryItem ) );
        }

        return set1;
    }

    protected Set<SalaryItem> salaryItemDTOSetToSalaryItemSet(Set<SalaryItemDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<SalaryItem> set1 = new HashSet<SalaryItem>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( SalaryItemDTO salaryItemDTO : set ) {
            set1.add( salaryItemMapper.toEntity( salaryItemDTO ) );
        }

        return set1;
    }

    protected Money moneyDtoToMoney(MoneyDto moneyDto) {
        if ( moneyDto == null ) {
            return null;
        }

        Money money = new Money();

        money.setCurrency( moneyDto.getCurrency() );
        money.setAmount( moneyDto.getAmount() );

        return money;
    }
}
