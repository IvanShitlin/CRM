package com.foxminded.hipsterfox.service.dto;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Objects;

public class EmailAddressDTO implements Serializable {

    private Long id;

    @Email
    private String address;

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
        if (this == o) return true;
        if (!(o instanceof EmailAddressDTO)) return false;
        EmailAddressDTO that = (EmailAddressDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "EmailAddressDTO{" +
            "id=" + id +
            ", address='" + address + '\'' +
            '}';
    }
}
