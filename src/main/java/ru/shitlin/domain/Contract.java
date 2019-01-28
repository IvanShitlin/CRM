package ru.shitlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.shitlin.domain.enumeration.CloseType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contractGenerator")
    @SequenceGenerator(name = "contractGenerator", sequenceName = "contract_sequence")
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "first_pay_date")
    private LocalDate firstPayDate;

    @Column(name = "next_pay_date")
    private LocalDate nextPayDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "close_type")
    private CloseType closeType;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Mentor mentor;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Agreement agreement;

    @Valid
    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @Valid
    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "mentor_rate_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "mentor_rate_currency"))
    })
    private Money mentorRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Contract startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Contract endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getFirstPayDate() {
        return firstPayDate;
    }

    public Contract firstPayDate(LocalDate firstPayDate) {
        this.firstPayDate = firstPayDate;
        return this;
    }

    public void setFirstPayDate(LocalDate firstPayDate) {
        this.firstPayDate = firstPayDate;
    }

    public LocalDate getNextPayDate() {
        return nextPayDate;
    }

    public Contract nextPayDate(LocalDate nextPayDate) {
        this.nextPayDate = nextPayDate;
        return this;
    }

    public void setNextPayDate(LocalDate nextPayDate) {
        this.nextPayDate = nextPayDate;
    }

    public CloseType getCloseType() {
        return closeType;
    }

    public Contract closeType(CloseType closeType) {
        this.closeType = closeType;
        return this;
    }

    public void setCloseType(CloseType closeType) {
        this.closeType = closeType;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public Contract mentor(Mentor mentor) {
        this.mentor = mentor;
        return this;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public Contract agreement(Agreement agreement) {
        this.agreement = agreement;
        return this;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public Money getPrice() {
        return price;
    }

    public Contract price(Money price){
        this.price = price;
        return this;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public Money getMentorRate() {
        return mentorRate;
    }

    public Contract mentorRate(Money mentorRate){
        this.mentorRate = mentorRate;
        return this;
    }

    public void setMentorRate(Money mentorRate) {
        this.mentorRate = mentorRate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contract contract = (Contract) o;
        if (contract.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contract.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contract{" +
            "id=" + id +
            ", startDate=" + startDate +
            ", mentor=" + mentor +
            ", agreement=" + agreement +
            ", price=" + price +
            '}';
    }
}
