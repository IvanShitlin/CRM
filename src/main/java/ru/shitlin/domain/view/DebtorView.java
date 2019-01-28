package ru.shitlin.domain.view;

import ru.shitlin.domain.Contract;
import ru.shitlin.domain.Invoice;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "debtor_view")
@Immutable
public class DebtorView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "invoice_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    @ManyToOne
    private Contract contract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DebtorView debtorsView = (DebtorView) o;
        if (debtorsView.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(id, debtorsView.id) &&
            Objects.equals(invoice, debtorsView.invoice) &&
            Objects.equals(contract, debtorsView.contract);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DebtorView{" +
            "id=" + id +
            ", invoice=" + invoice +
            ", contract=" + contract +
            '}';
    }
}
