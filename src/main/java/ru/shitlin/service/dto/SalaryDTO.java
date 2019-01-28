package ru.shitlin.service.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Salary entity.
 */
public class SalaryDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Boolean paid;

    private LocalDate paidDate;

    private Long mentorId;

    private String mentorFirstName;

    private String mentorLastName;

    private Set<SalaryItemDTO> items = new HashSet<>();

    @Valid
    @NotNull
    private MoneyDto sum;

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

    public Boolean isPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
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

    public MoneyDto getSum() {
        return sum;
    }

    public void setSum(MoneyDto sum) {
        this.sum = sum;
    }

    public Set<SalaryItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<SalaryItemDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SalaryDTO salaryDTO = (SalaryDTO) o;
        if (salaryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salaryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalaryDTO{" +
            "id=" + id +
            ", dateFrom=" + dateFrom +
            ", dateTo=" + dateTo +
            ", paid=" + paid +
            ", paidDate=" + paidDate +
            ", mentorId=" + mentorId +
            ", mentorFirstName=" + mentorFirstName +
            ", mentorLastName=" + mentorLastName +
            ", sum=" + sum +
            '}';
    }
}
