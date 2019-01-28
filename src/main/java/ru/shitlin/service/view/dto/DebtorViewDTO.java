package ru.shitlin.service.view.dto;

import ru.shitlin.service.dto.MoneyDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class DebtorViewDTO implements Serializable {

    private Long id;

    private String mentorFirstName;

    private String mentorLastName;

    private String clientFirstName;

    private String clientLastName;

    private String courseName;

    private LocalDate paymentDate;

    private MoneyDto sum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public MoneyDto getSum() {
        return sum;
    }

    public void setSum(MoneyDto sum) {
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DebtorViewDTO debtorViewDTO = (DebtorViewDTO) o;
        if (debtorViewDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), debtorViewDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DebtorViewDTO{" +
            "id=" + getId() +
            ", mentorFirstName='" + getMentorFirstName() + "'" +
            ", mentorLastName='" + getMentorLastName() + "'" +
            ", clientFirstName='" + getClientFirstName() + "'" +
            ", clientLastName='" + getClientLastName() + "'" +
            ", courseName='" + getCourseName() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", sum='" + getSum() + "'" +
            "}";
    }

}
