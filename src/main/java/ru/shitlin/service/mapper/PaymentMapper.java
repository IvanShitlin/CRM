package ru.shitlin.service.mapper;

import com.foxminded.hipsterfox.domain.*;
import ru.shitlin.domain.Payment;
import ru.shitlin.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Payment and its DTO PaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {InvoiceMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mappings({
        @Mapping(source = "invoice.id", target = "invoiceId"),
    })
    PaymentDTO toDto(Payment payment);

    @Mappings({
        @Mapping(source = "invoiceId", target = "invoice"),
    })
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
