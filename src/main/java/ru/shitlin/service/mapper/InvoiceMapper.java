package ru.shitlin.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import ru.shitlin.service.dto.InvoiceDTO;

import org.mapstruct.*;
import ru.shitlin.domain.Invoice;

/**
 * Mapper for the entity Invoice and its DTO InvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {ContractMapper.class, MoneyMapper.class, PaymentMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    @Mappings({
        @Mapping(source = "contract.id", target = "contractId"),
        @Mapping(source = "payment.id", target = "paymentId")
    })
    InvoiceDTO toDto(Invoice invoice);

    @Mappings({
        @Mapping(source = "contractId", target = "contract"),
        @Mapping(source = "paymentId", target = "payment")
    })
    Invoice toEntity(InvoiceDTO invoiceDTO);

    default Invoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(id);
        return invoice;
    }
}
