package ru.shitlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A SalaryItem.
 */
@Entity
@Table(name = "salary_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SalaryItem extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salaryItemGenerator")
    @SequenceGenerator(name = "salaryItemGenerator", sequenceName = "salary_item_sequence")
    private Long id;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    private LocalDate dateTo;

    @Column(name = "accounted")
    private Boolean accounted;

    @ManyToOne
    private Mentor mentor;

    @ManyToOne
    private Invoice invoice;

    @ManyToOne
    @JsonIgnoreProperties("items")
    private Salary salary;

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

    public SalaryItem dateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public SalaryItem dateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Boolean isAccounted() {
        return accounted;
    }

    public SalaryItem accounted(Boolean accounted) {
        this.accounted = accounted;
        return this;
    }

    public void setAccounted(Boolean accounted) {
        this.accounted = accounted;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public SalaryItem mentor(Mentor mentor) {
        this.mentor = mentor;
        return this;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public SalaryItem invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Salary getSalary() {
        return salary;
    }

    public SalaryItem salary(Salary salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public Money getSum() {
        return sum;
    }

    public SalaryItem sum(Money sum) {
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
        SalaryItem salaryItem = (SalaryItem) o;
        if (salaryItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salaryItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalaryItem{" +
            "id=" + id +
            ", dateFrom=" + dateFrom +
            ", dateTo=" + dateTo +
            ", mentorId=" + (mentor != null ? mentor.getId() : null) +
            ", invoiceId=" + (invoice != null ? invoice.getId() : null) +
            ", accounted=" + accounted +
            ", salaryId=" + (salary != null ? salary.getId() : null) +
            ", sum=" + sum +
            '}';
    }
}
