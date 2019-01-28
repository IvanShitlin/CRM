package ru.shitlin.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import ru.shitlin.service.dto.ContractDTO;

import org.mapstruct.*;
import ru.shitlin.domain.Contract;

/**
 * Mapper for the entity Contract and its DTO ContractDTO.
 */
@Mapper(componentModel = "spring", uses = {MentorMapper.class, AgreementMapper.class})
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {

    @Mappings({
        @Mapping(source = "mentor.id", target = "mentorId"),
        @Mapping(source = "agreement.id", target = "agreementId"),
        @Mapping(source = "agreement.course.name", target = "courseName"),
        @Mapping(source = "agreement.client.lastName", target = "clientLastName")
    })
    ContractDTO toDto(Contract contract);

    @Mappings({
        @Mapping(source = "mentorId", target = "mentor"),
        @Mapping(source = "agreementId", target = "agreement")
    })
    Contract toEntity(ContractDTO contractDTO);

    default Contract fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }
}
