package com.foxminded.hipsterfox.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Salary entity. This class is used in SalaryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /salaries?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SalaryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateFrom;

    private LocalDateFilter dateTo;

    private BooleanFilter paid;

    private LocalDateFilter paidDate;

    private LongFilter mentorId;

    private LongFilter itemsId;

    public SalaryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateFilter dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateFilter getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateFilter dateTo) {
        this.dateTo = dateTo;
    }

    public BooleanFilter getPaid() {
        return paid;
    }

    public void setPaid(BooleanFilter paid) {
        this.paid = paid;
    }

    public LocalDateFilter getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateFilter paidDate) {
        this.paidDate = paidDate;
    }

    public LongFilter getMentorId() {
        return mentorId;
    }

    public void setMentorId(LongFilter mentorId) {
        this.mentorId = mentorId;
    }

    public LongFilter getItemsId() {
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
    }

    @Override
    public String toString() {
        return "SalaryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateFrom != null ? "dateFrom=" + dateFrom + ", " : "") +
            (dateTo != null ? "dateTo=" + dateTo + ", " : "") +
            (paid != null ? "paid=" + paid + ", " : "") +
            (paidDate != null ? "paidDate=" + paidDate + ", " : "") +
            (mentorId != null ? "mentorId=" + mentorId + ", " : "") +
            (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
            "}";
    }

}
