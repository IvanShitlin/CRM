package ru.shitlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Salary.
 */
@Entity
@Table(name = "salary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Salary extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salaryGenerator")
    @SequenceGenerator(name = "salaryGenerator", sequenceName = "salary_sequence")
    private Long id;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    private LocalDate dateTo;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Mentor mentor;

    @OneToMany(mappedBy = "salary", cascade = CascadeType.MERGE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SalaryItem> items = new HashSet<>();

    @Valid
    @NotNull
    @Embedded
    private Money sum;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public Salary dateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public Salary dateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Boolean isPaid() {
        return paid;
    }

    public Salary paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public Salary paidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
        return this;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public Salary mentor(Mentor mentor) {
        this.mentor = mentor;
        return this;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public Set<SalaryItem> getItems() {
        return items;
    }

    public Salary items(Set<SalaryItem> salaryItems) {
        this.items = salaryItems;
        return this;
    }

    public Salary addItem(SalaryItem salaryItem) {
        this.items.add(salaryItem);
        salaryItem.setSalary(this);
        salaryItem.accounted(true);
        return this;
    }

    public Salary removeItem(SalaryItem salaryItem) {
        this.items.remove(salaryItem);
        salaryItem.setSalary(null);
        salaryItem.accounted(false);
        return this;
    }

    public void setItems(Set<SalaryItem> salaryItems) {
        this.items = salaryItems;
    }

    public Money getSum() {
        return sum;
    }

    public Salary sum(Money sum) {
        this.sum = sum;
        return this;
    }

    public void setSum(Money sum) {
        this.sum = sum;
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
        Salary salary = (Salary) o;
        if (salary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Salary{" +
            "id=" + id +
            ", dateFrom=" + dateFrom +
            ", dateTo=" + dateTo +
            ", paid=" + paid +
            ", paidDate=" + paidDate +
            ", mentorId="  + (mentor != null ? mentor.getId() : null) +
            ", sum=" + sum +
            '}';
    }
}
