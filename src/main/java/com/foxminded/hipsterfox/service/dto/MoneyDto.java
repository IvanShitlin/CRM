package com.foxminded.hipsterfox.service.dto;

import com.foxminded.hipsterfox.domain.enumeration.Currency;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class MoneyDto {

    @NotNull
    private Long amount;

    @NotNull
    private Currency currency;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyDto moneyDto = (MoneyDto) o;
        return Objects.equals(amount, moneyDto.amount) &&
            currency == moneyDto.currency;
    }

    @Override
    public int hashCode() {

        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
