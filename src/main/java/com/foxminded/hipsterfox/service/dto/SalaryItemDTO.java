package com.foxminded.hipsterfox.service.dto;

import com.foxminded.hipsterfox.domain.enumeration.Currency;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SalaryItem entity.
 */
public class SalaryItemDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Boolean accounted;

    private Long mentorId;

    private String mentorFirstName;

    private String mentorLastName;

    private Long invoiceId;

    private Long salaryId;

    @Valid
    @NotNull
    private MoneyDto sum;

    public SalaryItemDTO() {
    }

    public SalaryItemDTO(Long id) {
        this.id = id;
    }

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

    public Boolean isAccounted() {
        return accounted;
    }

    public void setAccounted(Boolean accounted) {
        this.accounted = accounted;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Long salaryId) {
        this.salaryId = salaryId;
    }

    public MoneyDto getSum() {
        return sum;
    }

    public void setSum(MoneyDto sum) {
        this.sum = sum;
    }

    public String getMentorFirstName() {
        return mentorFirstName;
    }

    public void setMentorFirstName(String mentorFirstName) {
        this.mentorFirstName = mentorFirstName;
    }

    public String getMentorLastName() {
        return mentorLastName;
    }

    public void setMentorLastName(String mentorLastName) {
        this.mentorLastName = mentorLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SalaryItemDTO salaryItemDTO = (SalaryItemDTO) o;
        if (salaryItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salaryItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalaryItemDTO{" +
            "id=" + id +
            ", dateFrom=" + dateFrom +
            ", dateTo=" + dateTo +
            ", accounted=" + accounted +
            ", mentorId=" + mentorId +
            ", mentorFirstName='" + getMentorFirstName() + "'" +
            ", mentorLastName='" + getMentorLastName() + "'" +
            ", invoiceId=" + invoiceId +
            ", salaryId=" + salaryId +
            ", sum=" + sum +
            '}';
    }
}
