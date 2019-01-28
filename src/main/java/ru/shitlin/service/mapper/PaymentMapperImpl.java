package ru.shitlin.service.mapper;

import ru.shitlin.domain.Invoice;
import ru.shitlin.domain.Money;
import ru.shitlin.domain.Payment;
import ru.shitlin.service.dto.MoneyDto;
import ru.shitlin.service.dto.PaymentDTO;
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
public class PaymentMapperImpl implements PaymentMapper {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Override
    public List<Payment> toEntity(List<PaymentDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Payment> list = new ArrayList<Payment>( dtoList.size() );
        for ( PaymentDTO paymentDTO : dtoList ) {
            list.add( toEntity( paymentDTO ) );
        }

        return list;
    }

    @Override
    public List<PaymentDTO> toDto(List<Payment> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<PaymentDTO> list = new ArrayList<PaymentDTO>( entityList.size() );
        for ( Payment payment : entityList ) {
            list.add( toDto( payment ) );
        }

        return list;
    }

    @Override
    public PaymentDTO toDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentDTO paymentDTO = new PaymentDTO();

        Long id = paymentInvoiceId( payment );
        if ( id != null ) {
            paymentDTO.setInvoiceId( id );
        }
        paymentDTO.setCreatedBy( payment.getCreatedBy() );
        paymentDTO.setCreatedDate( payment.getCreatedDate() );
        paymentDTO.setLastModifiedBy( payment.getLastModifiedBy() );
        paymentDTO.setLastModifiedDate( payment.getLastModifiedDate() );
        paymentDTO.setId( payment.getId() );
        paymentDTO.setDate( payment.getDate() );
        paymentDTO.setSum( moneyToMoneyDto( payment.getSum() ) );

        return paymentDTO;
    }

    @Override
    public Payment toEntity(PaymentDTO paymentDTO) {
        if ( paymentDTO == null ) {
            return null;
        }

        Payment payment = new Payment();

        payment.setInvoice( invoiceMapper.fromId( paymentDTO.getInvoiceId() ) );
        payment.setCreatedBy( paymentDTO.getCreatedBy() );
        payment.setCreatedDate( paymentDTO.getCreatedDate() );
        payment.setLastModifiedBy( paymentDTO.getLastModifiedBy() );
        payment.setLastModifiedDate( paymentDTO.getLastModifiedDate() );
        payment.setId( paymentDTO.getId() );
        payment.setDate( paymentDTO.getDate() );
        payment.setSum( moneyDtoToMoney( paymentDTO.getSum() ) );

        return payment;
    }

    private Long paymentInvoiceId(Payment payment) {
        if ( payment == null ) {
            return null;
        }
        Invoice invoice = payment.getInvoice();
        if ( invoice == null ) {
            return null;
        }
        Long id = invoice.getId();
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
