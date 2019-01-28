package ru.shitlin.service.mapper;

import ru.shitlin.domain.Contract;
import ru.shitlin.domain.Invoice;
import ru.shitlin.domain.Payment;
import ru.shitlin.service.dto.InvoiceDTO;
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
public class InvoiceMapperImpl implements InvoiceMapper {

    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private MoneyMapper moneyMapper;
    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public List<Invoice> toEntity(List<InvoiceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Invoice> list = new ArrayList<Invoice>( dtoList.size() );
        for ( InvoiceDTO invoiceDTO : dtoList ) {
            list.add( toEntity( invoiceDTO ) );
        }

        return list;
    }

    @Override
    public List<InvoiceDTO> toDto(List<Invoice> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<InvoiceDTO> list = new ArrayList<InvoiceDTO>( entityList.size() );
        for ( Invoice invoice : entityList ) {
            list.add( toDto( invoice ) );
        }

        return list;
    }

    @Override
    public InvoiceDTO toDto(Invoice invoice) {
        if ( invoice == null ) {
            return null;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();

        Long id = invoiceContractId( invoice );
        if ( id != null ) {
            invoiceDTO.setContractId( id );
        }
        Long id1 = invoicePaymentId( invoice );
        if ( id1 != null ) {
            invoiceDTO.setPaymentId( id1 );
        }
        invoiceDTO.setCreatedBy( invoice.getCreatedBy() );
        invoiceDTO.setCreatedDate( invoice.getCreatedDate() );
        invoiceDTO.setLastModifiedBy( invoice.getLastModifiedBy() );
        invoiceDTO.setLastModifiedDate( invoice.getLastModifiedDate() );
        invoiceDTO.setId( invoice.getId() );
        invoiceDTO.setDateFrom( invoice.getDateFrom() );
        invoiceDTO.setDateTo( invoice.getDateTo() );
        invoiceDTO.setSum( moneyMapper.toDto( invoice.getSum() ) );

        return invoiceDTO;
    }

    @Override
    public Invoice toEntity(InvoiceDTO invoiceDTO) {
        if ( invoiceDTO == null ) {
            return null;
        }

        Invoice invoice = new Invoice();

        invoice.setPayment( paymentMapper.fromId( invoiceDTO.getPaymentId() ) );
        invoice.setContract( contractMapper.fromId( invoiceDTO.getContractId() ) );
        invoice.setCreatedBy( invoiceDTO.getCreatedBy() );
        invoice.setCreatedDate( invoiceDTO.getCreatedDate() );
        invoice.setLastModifiedBy( invoiceDTO.getLastModifiedBy() );
        invoice.setLastModifiedDate( invoiceDTO.getLastModifiedDate() );
        invoice.setId( invoiceDTO.getId() );
        invoice.setDateFrom( invoiceDTO.getDateFrom() );
        invoice.setDateTo( invoiceDTO.getDateTo() );
        invoice.setSum( moneyMapper.toEntity( invoiceDTO.getSum() ) );

        return invoice;
    }

    private Long invoiceContractId(Invoice invoice) {
        if ( invoice == null ) {
            return null;
        }
        Contract contract = invoice.getContract();
        if ( contract == null ) {
            return null;
        }
        Long id = contract.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long invoicePaymentId(Invoice invoice) {
        if ( invoice == null ) {
            return null;
        }
        Payment payment = invoice.getPayment();
        if ( payment == null ) {
            return null;
        }
        Long id = payment.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
