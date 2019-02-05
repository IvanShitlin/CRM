package com.foxminded.hipsterfox.service.view.mapper;

import com.foxminded.hipsterfox.domain.view.DebtorView;
import com.foxminded.hipsterfox.service.mapper.EntityMapper;
import com.foxminded.hipsterfox.service.view.dto.DebtorViewDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {})
public interface DebtorViewMapper extends EntityMapper<DebtorViewDTO, DebtorView> {

    @Mappings({
        @Mapping(source = "invoice.id", target = "id"),
        @Mapping(source = "contract.agreement.course.name", target = "courseName"),
        @Mapping(source = "contract.mentor.firstName", target = "mentorFirstName"),
        @Mapping(source = "contract.mentor.lastName", target = "mentorLastName"),
        @Mapping(source = "contract.agreement.client.firstName", target = "clientFirstName"),
        @Mapping(source = "contract.agreement.client.lastName", target = "clientLastName"),
        @Mapping(source = "contract.nextPayDate", target = "paymentDate"),
        @Mapping(source = "contract.price", target = "sum")
    })
    DebtorViewDTO toDto(DebtorView debtorView);

    default DebtorView fromId(Long id) {
        if (id == null) {
            return null;
        }
        DebtorView debtorView = new DebtorView();
        debtorView.setId(id);
        return debtorView;
    }

}
