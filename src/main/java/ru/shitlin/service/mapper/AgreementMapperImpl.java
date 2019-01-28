package ru.shitlin.service.mapper;

import ru.shitlin.domain.Agreement;
import ru.shitlin.domain.Client;
import ru.shitlin.domain.Course;
import ru.shitlin.domain.CourseType;
import ru.shitlin.domain.Money;
import ru.shitlin.service.dto.AgreementDTO;
import ru.shitlin.service.dto.CourseTypeDTO;
import ru.shitlin.service.dto.MoneyDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:10+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class AgreementMapperImpl implements AgreementMapper {

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Agreement> toEntity(List<AgreementDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Agreement> list = new ArrayList<Agreement>( dtoList.size() );
        for ( AgreementDTO agreementDTO : dtoList ) {
            list.add( toEntity( agreementDTO ) );
        }

        return list;
    }

    @Override
    public List<AgreementDTO> toDto(List<Agreement> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AgreementDTO> list = new ArrayList<AgreementDTO>( entityList.size() );
        for ( Agreement agreement : entityList ) {
            list.add( toDto( agreement ) );
        }

        return list;
    }

    @Override
    public AgreementDTO toDto(Agreement agreement) {
        if ( agreement == null ) {
            return null;
        }

        AgreementDTO agreementDTO = new AgreementDTO();

        String firstName = agreementClientFirstName( agreement );
        if ( firstName != null ) {
            agreementDTO.setClientFirstName( firstName );
        }
        String name = agreementCourseName( agreement );
        if ( name != null ) {
            agreementDTO.setCourseName( name );
        }
        String lastName = agreementClientLastName( agreement );
        if ( lastName != null ) {
            agreementDTO.setClientLastName( lastName );
        }
        Long id = agreementClientId( agreement );
        if ( id != null ) {
            agreementDTO.setClientId( id );
        }
        Long id1 = agreementCourseId( agreement );
        if ( id1 != null ) {
            agreementDTO.setCourseId( id1 );
        }
        agreementDTO.setCreatedBy( agreement.getCreatedBy() );
        agreementDTO.setCreatedDate( agreement.getCreatedDate() );
        agreementDTO.setLastModifiedBy( agreement.getLastModifiedBy() );
        agreementDTO.setLastModifiedDate( agreement.getLastModifiedDate() );
        agreementDTO.setCourseType( courseTypeToCourseTypeDTO( agreement.getCourseType() ) );
        agreementDTO.setId( agreement.getId() );
        agreementDTO.setStartDate( agreement.getStartDate() );
        agreementDTO.setEndDate( agreement.getEndDate() );
        agreementDTO.setStatus( agreement.getStatus() );

        return agreementDTO;
    }

    @Override
    public Agreement toEntity(AgreementDTO agreementDTO) {
        if ( agreementDTO == null ) {
            return null;
        }

        Agreement agreement = new Agreement();

        agreement.setClient( clientMapper.fromId( agreementDTO.getClientId() ) );
        agreement.setCourse( courseMapper.fromId( agreementDTO.getCourseId() ) );
        agreement.setCreatedBy( agreementDTO.getCreatedBy() );
        agreement.setCreatedDate( agreementDTO.getCreatedDate() );
        agreement.setLastModifiedBy( agreementDTO.getLastModifiedBy() );
        agreement.setLastModifiedDate( agreementDTO.getLastModifiedDate() );
        agreement.setId( agreementDTO.getId() );
        agreement.setStartDate( agreementDTO.getStartDate() );
        agreement.setEndDate( agreementDTO.getEndDate() );
        agreement.setCourseType( courseTypeDTOToCourseType( agreementDTO.getCourseType() ) );

        return agreement;
    }

    @Override
    public Agreement updateAgreementFromDto(AgreementDTO agreementDTO, Agreement agreement) {
        if ( agreementDTO == null ) {
            return null;
        }

        agreement.setClient( clientMapper.fromId( agreementDTO.getClientId() ) );
        agreement.setCourse( courseMapper.fromId( agreementDTO.getCourseId() ) );
        agreement.setCreatedBy( agreementDTO.getCreatedBy() );
        agreement.setCreatedDate( agreementDTO.getCreatedDate() );
        agreement.setLastModifiedBy( agreementDTO.getLastModifiedBy() );
        agreement.setLastModifiedDate( agreementDTO.getLastModifiedDate() );
        agreement.setId( agreementDTO.getId() );
        agreement.setStartDate( agreementDTO.getStartDate() );
        agreement.setEndDate( agreementDTO.getEndDate() );
        if ( agreementDTO.getCourseType() != null ) {
            if ( agreement.getCourseType() == null ) {
                agreement.setCourseType( new CourseType() );
            }
            courseTypeDTOToCourseType1( agreementDTO.getCourseType(), agreement.getCourseType() );
        }
        else {
            agreement.setCourseType( null );
        }

        return agreement;
    }

    private String agreementClientFirstName(Agreement agreement) {
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

    private String agreementCourseName(Agreement agreement) {
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

    private String agreementClientLastName(Agreement agreement) {
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

    private Long agreementClientId(Agreement agreement) {
        if ( agreement == null ) {
            return null;
        }
        Client client = agreement.getClient();
        if ( client == null ) {
            return null;
        }
        Long id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long agreementCourseId(Agreement agreement) {
        if ( agreement == null ) {
            return null;
        }
        Course course = agreement.getCourse();
        if ( course == null ) {
            return null;
        }
        Long id = course.getId();
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

    protected CourseTypeDTO courseTypeToCourseTypeDTO(CourseType courseType) {
        if ( courseType == null ) {
            return null;
        }

        CourseTypeDTO courseTypeDTO = new CourseTypeDTO();

        courseTypeDTO.setId( courseType.getId() );
        courseTypeDTO.setType( courseType.getType() );
        courseTypeDTO.setLocation( courseType.getLocation() );
        courseTypeDTO.setPrice( moneyToMoneyDto( courseType.getPrice() ) );
        courseTypeDTO.setMentorRate( moneyToMoneyDto( courseType.getMentorRate() ) );

        return courseTypeDTO;
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

    protected CourseType courseTypeDTOToCourseType(CourseTypeDTO courseTypeDTO) {
        if ( courseTypeDTO == null ) {
            return null;
        }

        CourseType courseType = new CourseType();

        courseType.setId( courseTypeDTO.getId() );
        courseType.setType( courseTypeDTO.getType() );
        courseType.setLocation( courseTypeDTO.getLocation() );
        courseType.setPrice( moneyDtoToMoney( courseTypeDTO.getPrice() ) );
        courseType.setMentorRate( moneyDtoToMoney( courseTypeDTO.getMentorRate() ) );

        return courseType;
    }

    protected void moneyDtoToMoney1(MoneyDto moneyDto, Money mappingTarget) {
        if ( moneyDto == null ) {
            return;
        }

        mappingTarget.setCurrency( moneyDto.getCurrency() );
        mappingTarget.setAmount( moneyDto.getAmount() );
    }

    protected void courseTypeDTOToCourseType1(CourseTypeDTO courseTypeDTO, CourseType mappingTarget) {
        if ( courseTypeDTO == null ) {
            return;
        }

        mappingTarget.setId( courseTypeDTO.getId() );
        mappingTarget.setType( courseTypeDTO.getType() );
        mappingTarget.setLocation( courseTypeDTO.getLocation() );
        if ( courseTypeDTO.getPrice() != null ) {
            if ( mappingTarget.getPrice() == null ) {
                mappingTarget.setPrice( new Money() );
            }
            moneyDtoToMoney1( courseTypeDTO.getPrice(), mappingTarget.getPrice() );
        }
        else {
            mappingTarget.setPrice( null );
        }
        if ( courseTypeDTO.getMentorRate() != null ) {
            if ( mappingTarget.getMentorRate() == null ) {
                mappingTarget.setMentorRate( new Money() );
            }
            moneyDtoToMoney1( courseTypeDTO.getMentorRate(), mappingTarget.getMentorRate() );
        }
        else {
            mappingTarget.setMentorRate( null );
        }
    }
}
