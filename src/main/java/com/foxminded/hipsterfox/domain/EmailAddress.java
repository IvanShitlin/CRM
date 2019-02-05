package com.foxminded.hipsterfox.domain;

import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "email_address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmailAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailAddressGenerator")
    @SequenceGenerator(name = "emailAddressGenerator", sequenceName = "email_address_sequence")
    private Long id;

    @NotNull
    @Email
    @Column(name = "address", unique = true)
    private String address;

    public EmailAddress() {
    }

    public EmailAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailAddress emailAddress = (EmailAddress) o;
        if (emailAddress.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailAddress.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailAddress{" +
            "id=" + id +
            ", address=" + address +
            "}";
    }
}
