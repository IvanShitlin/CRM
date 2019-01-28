package ru.shitlin.service.mapper;

import ru.shitlin.domain.Invoice;
import ru.shitlin.domain.Mentor;
import ru.shitlin.domain.Money;
import ru.shitlin.domain.Salary;
import ru.shitlin.domain.SalaryItem;
import ru.shitlin.service.dto.MoneyDto;
import ru.shitlin.service.dto.SalaryItemDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:09+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class SalaryItemMapperImpl implements SalaryItemMapper {

    @Autowired
    private MentorMapper mentorMapper;
    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private SalaryMapper salaryMapper;

    @Override
    public List<SalaryItem> toEntity(List<SalaryItemDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<SalaryItem> list = new ArrayList<SalaryItem>( dtoList.size() );
        for ( SalaryItemDTO salaryItemDTO : dtoList ) {
            list.add( toEntity( salaryItemDTO ) );
        }

        return list;
    }

    @Override
    public List<SalaryItemDTO> toDto(List<SalaryItem> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SalaryItemDTO> list = new ArrayList<SalaryItemDTO>( entityList.size() );
        for ( SalaryItem salaryItem : entityList ) {
            list.add( toDto( salaryItem ) );
        }

        return list;
    }

    @Override
    public SalaryItemDTO toDto(SalaryItem salaryItem) {
        if ( salaryItem == null ) {
            return null;
        }

        SalaryItemDTO salaryItemDTO = new SalaryItemDTO();

        String lastName = salaryItemMentorLastName( salaryItem );
        if ( lastName != null ) {
            salaryItemDTO.setMentorLastName( lastName );
        }
        String firstName = salaryItemMentorFirstName( salaryItem );
        if ( firstName != null ) {
            salaryItemDTO.setMentorFirstName( firstName );
        }
        Long id = salaryItemInvoiceId( salaryItem );
        if ( id != null ) {
            salaryItemDTO.setInvoiceId( id );
        }
        Long id1 = salaryItemMentorId( salaryItem );
        if ( id1 != null ) {
            salaryItemDTO.setMentorId( id1 );
        }
        Long id2 = salaryItemSalaryId( salaryItem );
        if ( id2 != null ) {
            salaryItemDTO.setSalaryId( id2 );
        }
        salaryItemDTO.setCreatedBy( salaryItem.getCreatedBy() );
        salaryItemDTO.setCreatedDate( salaryItem.getCreatedDate() );
        salaryItemDTO.setLastModifiedBy( salaryItem.getLastModifiedBy() );
        salaryItemDTO.setLastModifiedDate( salaryItem.getLastModifiedDate() );
        salaryItemDTO.setId( salaryItem.getId() );
        salaryItemDTO.setDateFrom( salaryItem.getDateFrom() );
        salaryItemDTO.setDateTo( salaryItem.getDateTo() );
        salaryItemDTO.setAccounted( salaryItem.isAccounted() );
        salaryItemDTO.setSum( moneyToMoneyDto( salaryItem.getSum() ) );

        return salaryItemDTO;
    }

    @Override
    public SalaryItem toEntity(SalaryItemDTO salaryItemDTO) {
        if ( salaryItemDTO == null ) {
            return null;
        }

        SalaryItem salaryItem = new SalaryItem();

        salaryItem.setMentor( mentorMapper.fromId( salaryItemDTO.getMentorId() ) );
        salaryItem.setInvoice( invoiceMapper.fromId( salaryItemDTO.getInvoiceId() ) );
        salaryItem.setSalary( salaryMapper.fromId( salaryItemDTO.getSalaryId() ) );
        salaryItem.setCreatedBy( salaryItemDTO.getCreatedBy() );
        salaryItem.setCreatedDate( salaryItemDTO.getCreatedDate() );
        salaryItem.setLastModifiedBy( salaryItemDTO.getLastModifiedBy() );
        salaryItem.setLastModifiedDate( salaryItemDTO.getLastModifiedDate() );
        salaryItem.setId( salaryItemDTO.getId() );
        salaryItem.setDateFrom( salaryItemDTO.getDateFrom() );
        salaryItem.setDateTo( salaryItemDTO.getDateTo() );
        salaryItem.setAccounted( salaryItemDTO.isAccounted() );
        salaryItem.setSum( moneyDtoToMoney( salaryItemDTO.getSum() ) );

        return salaryItem;
    }

    private String salaryItemMentorLastName(SalaryItem salaryItem) {
        if ( salaryItem == null ) {
            return null;
        }
        Mentor mentor = salaryItem.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String lastName = mentor.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String salaryItemMentorFirstName(SalaryItem salaryItem) {
        if ( salaryItem == null ) {
            return null;
        }
        Mentor mentor = salaryItem.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String firstName = mentor.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private Long salaryItemInvoiceId(SalaryItem salaryItem) {
        if ( salaryItem == null ) {
            return null;
        }
        Invoice invoice = salaryItem.getInvoice();
        if ( invoice == null ) {
            return null;
        }
        Long id = invoice.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long salaryItemMentorId(SalaryItem salaryItem) {
        if ( salaryItem == null ) {
            return null;
        }
        Mentor mentor = salaryItem.getMentor();
        if ( mentor == null ) {
            return null;
        }
        Long id = mentor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long salaryItemSalaryId(SalaryItem salaryItem) {
        if ( salaryItem == null ) {
            return null;
        }
        Salary salary = salaryItem.getSalary();
        if ( salary == null ) {
            return null;
        }
        Long id = salary.getId();
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
