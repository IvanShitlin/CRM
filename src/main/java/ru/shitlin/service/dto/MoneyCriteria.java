package ru.shitlin.service.dto;

import ru.shitlin.domain.enumeration.Currency;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;

import java.io.Serializable;

/**
 * Criteria class for the Money entity. This class is used in *Resource classes to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contracts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MoneyCriteria implements Serializable {
    /**
     * Class for filtering Currency
     */
    public static class CurrencyFilter extends Filter<Currency> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter amount;

    private CurrencyFilter currency;

    public MoneyCriteria() {
    }

    public LongFilter getAmount() {
        return amount;
    }

    public void setAmount(LongFilter amount) {
        this.amount = amount;
    }

    public CurrencyFilter getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyFilter currency) {
        this.currency = currency;
    }

    public String toString(String fieldName) {
        return (amount != null ? fieldName + ".amount=" + amount + ", " : "") +
            (currency != null ? fieldName + ".currency=" + currency + ", " : "");
    }

}
