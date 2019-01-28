package ru.shitlin.service.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Invoice entity.
 */
public class InvoiceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Long contractId;

    @Valid
    @NotNull
    private MoneyDto sum;

    private Long paymentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public MoneyDto getSum() {
        return sum;
    }

    public void setSum(MoneyDto sum) {
        this.sum = sum;
    }

    public Long getPaymentId() { return paymentId; }

    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (invoiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + id +
            ", dateFrom=" + dateFrom +
            ", dateTo=" + dateTo +
            ", contractId=" + contractId +
            ", sum=" + sum +
            ", paymentId=" + paymentId +
            '}';
    }
}
