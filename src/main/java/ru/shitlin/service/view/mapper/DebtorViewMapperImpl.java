package ru.shitlin.service.view.mapper;

import ru.shitlin.domain.Agreement;
import ru.shitlin.domain.Client;
import ru.shitlin.domain.Contract;
import ru.shitlin.domain.Course;
import ru.shitlin.domain.Invoice;
import ru.shitlin.domain.Mentor;
import ru.shitlin.domain.Money;
import ru.shitlin.domain.view.DebtorView;
import ru.shitlin.service.dto.MoneyDto;
import ru.shitlin.service.view.dto.DebtorViewDTO;
import java.time.LocalDate;
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
public class DebtorViewMapperImpl implements DebtorViewMapper {

    @Override
    public DebtorView toEntity(DebtorViewDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DebtorView debtorView = new DebtorView();

        debtorView.setId( dto.getId() );

        return debtorView;
    }

    @Override
    public List<DebtorView> toEntity(List<DebtorViewDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DebtorView> list = new ArrayList<DebtorView>( dtoList.size() );
        for ( DebtorViewDTO debtorViewDTO : dtoList ) {
            list.add( toEntity( debtorViewDTO ) );
        }

        return list;
    }

    @Override
    public List<DebtorViewDTO> toDto(List<DebtorView> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DebtorViewDTO> list = new ArrayList<DebtorViewDTO>( entityList.size() );
        for ( DebtorView debtorView : entityList ) {
            list.add( toDto( debtorView ) );
        }

        return list;
    }

    @Override
    public DebtorViewDTO toDto(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }

        DebtorViewDTO debtorViewDTO = new DebtorViewDTO();

        String lastName = debtorViewContractMentorLastName( debtorView );
        if ( lastName != null ) {
            debtorViewDTO.setMentorLastName( lastName );
        }
        String name = debtorViewContractAgreementCourseName( debtorView );
        if ( name != null ) {
            debtorViewDTO.setCourseName( name );
        }
        String lastName1 = debtorViewContractAgreementClientLastName( debtorView );
        if ( lastName1 != null ) {
            debtorViewDTO.setClientLastName( lastName1 );
        }
        String firstName = debtorViewContractAgreementClientFirstName( debtorView );
        if ( firstName != null ) {
            debtorViewDTO.setClientFirstName( firstName );
        }
        String firstName1 = debtorViewContractMentorFirstName( debtorView );
        if ( firstName1 != null ) {
            debtorViewDTO.setMentorFirstName( firstName1 );
        }
        Money price = debtorViewContractPrice( debtorView );
        if ( price != null ) {
            debtorViewDTO.setSum( moneyToMoneyDto( price ) );
        }
        Long id = debtorViewInvoiceId( debtorView );
        if ( id != null ) {
            debtorViewDTO.setId( id );
        }
        LocalDate nextPayDate = debtorViewContractNextPayDate( debtorView );
        if ( nextPayDate != null ) {
            debtorViewDTO.setPaymentDate( nextPayDate );
        }

        return debtorViewDTO;
    }

    private String debtorViewContractMentorLastName(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }
        Contract contract = debtorView.getContract();
        if ( contract == null ) {
            return null;
        }
        Mentor mentor = contract.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String lastName = mentor.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String debtorViewContractAgreementCourseName(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }
        Contract contract = debtorView.getContract();
        if ( contract == null ) {
            return null;
        }
        Agreement agreement = contract.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        Course course = agreement.getCourse();
        if ( course == null ) {
            return null;
        }
        String name = course.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String debtorViewContractAgreementClientLastName(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }
        Contract contract = debtorView.getContract();
        if ( contract == null ) {
            return null;
        }
        Agreement agreement = contract.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        Client client = agreement.getClient();
        if ( client == null ) {
            return null;
        }
        String lastName = client.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String debtorViewContractAgreementClientFirstName(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }
        Contract contract = debtorView.getContract();
        if ( contract == null ) {
            return null;
        }
        Agreement agreement = contract.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        Client client = agreement.getClient();
        if ( client == null ) {
            return null;
        }
        String firstName = client.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String debtorViewContractMentorFirstName(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }
        Contract contract = debtorView.getContract();
        if ( contract == null ) {
            return null;
        }
        Mentor mentor = contract.getMentor();
        if ( mentor == null ) {
            return null;
        }
        String firstName = mentor.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private Money debtorViewContractPrice(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }
        Contract contract = debtorView.getContract();
        if ( contract == null ) {
            return null;
        }
        Money price = contract.getPrice();
        if ( price == null ) {
            return null;
        }
        return price;
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

    private Long debtorViewInvoiceId(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }
        Invoice invoice = debtorView.getInvoice();
        if ( invoice == null ) {
            return null;
        }
        Long id = invoice.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private LocalDate debtorViewContractNextPayDate(DebtorView debtorView) {
        if ( debtorView == null ) {
            return null;
        }
        Contract contract = debtorView.getContract();
        if ( contract == null ) {
            return null;
        }
        LocalDate nextPayDate = contract.getNextPayDate();
        if ( nextPayDate == null ) {
            return null;
        }
        return nextPayDate;
    }
}
