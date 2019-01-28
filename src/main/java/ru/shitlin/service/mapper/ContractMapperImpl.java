package ru.shitlin.service.mapper;

import ru.shitlin.domain.Agreement;
import ru.shitlin.domain.Client;
import ru.shitlin.domain.Contract;
import ru.shitlin.domain.Course;
import ru.shitlin.domain.Mentor;
import ru.shitlin.domain.Money;
import ru.shitlin.service.dto.ContractDTO;
import ru.shitlin.service.dto.MoneyDto;
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
public class ContractMapperImpl implements ContractMapper {

    @Autowired
    private MentorMapper mentorMapper;
    @Autowired
    private AgreementMapper agreementMapper;

    @Override
    public List<Contract> toEntity(List<ContractDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Contract> list = new ArrayList<Contract>( dtoList.size() );
        for ( ContractDTO contractDTO : dtoList ) {
            list.add( toEntity( contractDTO ) );
        }

        return list;
    }

    @Override
    public List<ContractDTO> toDto(List<Contract> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ContractDTO> list = new ArrayList<ContractDTO>( entityList.size() );
        for ( Contract contract : entityList ) {
            list.add( toDto( contract ) );
        }

        return list;
    }

    @Override
    public ContractDTO toDto(Contract contract) {
        if ( contract == null ) {
            return null;
        }

        ContractDTO contractDTO = new ContractDTO();

        Long id = contractAgreementId( contract );
        if ( id != null ) {
            contractDTO.setAgreementId( id );
        }
        String name = contractAgreementCourseName( contract );
        if ( name != null ) {
            contractDTO.setCourseName( name );
        }
        String lastName = contractAgreementClientLastName( contract );
        if ( lastName != null ) {
            contractDTO.setClientLastName( lastName );
        }
        Long id1 = contractMentorId( contract );
        if ( id1 != null ) {
            contractDTO.setMentorId( id1 );
        }
        contractDTO.setCreatedBy( contract.getCreatedBy() );
        contractDTO.setCreatedDate( contract.getCreatedDate() );
        contractDTO.setLastModifiedBy( contract.getLastModifiedBy() );
        contractDTO.setLastModifiedDate( contract.getLastModifiedDate() );
        contractDTO.setId( contract.getId() );
        contractDTO.setStartDate( contract.getStartDate() );
        contractDTO.setEndDate( contract.getEndDate() );
        contractDTO.setFirstPayDate( contract.getFirstPayDate() );
        contractDTO.setNextPayDate( contract.getNextPayDate() );
        contractDTO.setCloseType( contract.getCloseType() );
        contractDTO.setPrice( moneyToMoneyDto( contract.getPrice() ) );
        contractDTO.setMentorRate( moneyToMoneyDto( contract.getMentorRate() ) );

        return contractDTO;
    }

    @Override
    public Contract toEntity(ContractDTO contractDTO) {
        if ( contractDTO == null ) {
            return null;
        }

        Contract contract = new Contract();

        contract.setMentor( mentorMapper.fromId( contractDTO.getMentorId() ) );
        contract.setAgreement( agreementMapper.fromId( contractDTO.getAgreementId() ) );
        contract.setCreatedBy( contractDTO.getCreatedBy() );
        contract.setCreatedDate( contractDTO.getCreatedDate() );
        contract.setLastModifiedBy( contractDTO.getLastModifiedBy() );
        contract.setLastModifiedDate( contractDTO.getLastModifiedDate() );
        contract.setId( contractDTO.getId() );
        contract.setStartDate( contractDTO.getStartDate() );
        contract.setEndDate( contractDTO.getEndDate() );
        contract.setFirstPayDate( contractDTO.getFirstPayDate() );
        contract.setNextPayDate( contractDTO.getNextPayDate() );
        contract.setCloseType( contractDTO.getCloseType() );
        contract.setPrice( moneyDtoToMoney( contractDTO.getPrice() ) );
        contract.setMentorRate( moneyDtoToMoney( contractDTO.getMentorRate() ) );

        return contract;
    }

    private Long contractAgreementId(Contract contract) {
        if ( contract == null ) {
            return null;
        }
        Agreement agreement = contract.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        Long id = agreement.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String contractAgreementCourseName(Contract contract) {
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

    private String contractAgreementClientLastName(Contract contract) {
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

    private Long contractMentorId(Contract contract) {
        if ( contract == null ) {
            return null;
        }
        Mentor mentor = contract.getMentor();
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
