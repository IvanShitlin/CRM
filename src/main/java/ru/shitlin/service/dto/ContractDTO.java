package ru.shitlin.service.dto;

import ru.shitlin.domain.enumeration.CloseType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Contract entity.
 */
public class ContractDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate firstPayDate;

    private LocalDate nextPayDate;

    private CloseType closeType;

    private Long mentorId;

    private Long agreementId;

    private String courseName;

    private String clientLastName;

    @Valid
    @NotNull
    private MoneyDto price;

    @Valid
    @NotNull
    private MoneyDto mentorRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getFirstPayDate() {
        return firstPayDate;
    }

    public void setFirstPayDate(LocalDate firstPayDate) {
        this.firstPayDate = firstPayDate;
    }

    public LocalDate getNextPayDate() {
        return nextPayDate;
    }

    public void setNextPayDate(LocalDate nextPayDate) {
        this.nextPayDate = nextPayDate;
    }

    public CloseType getCloseType() {
        return closeType;
    }

    public void setCloseType(CloseType closeType) {
        this.closeType = closeType;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public MoneyDto getPrice() {
        return price;
    }

    public void setPrice(MoneyDto price) {
        this.price = price;
    }

    public MoneyDto getMentorRate() {
        return mentorRate;
    }

    public void setMentorRate(MoneyDto mentorRate) {
        this.mentorRate = mentorRate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (contractDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contractDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContractDTO{" +
            "id=" + id +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", firstPayDate=" + firstPayDate +
            ", nextPayDate=" + nextPayDate +
            ", closeType=" + closeType +
            ", mentorId=" + mentorId +
            ", agreementId=" + agreementId +
            ", price=" + price +
            ", mentorRate=" + mentorRate +
            ", courseName=" + courseName +
            ", clientLastName=" + clientLastName +
            '}';
    }
}
