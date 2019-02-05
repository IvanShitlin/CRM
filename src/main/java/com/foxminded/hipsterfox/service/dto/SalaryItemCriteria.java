package com.foxminded.hipsterfox.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the SalaryItem entity. This class is used in SalaryItemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /salary-items?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SalaryItemCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateFrom;

    private LocalDateFilter dateTo;

    private BooleanFilter accounted;

    private LongFilter mentorId;

    private LongFilter invoiceId;

    private LongFilter salaryId;

    public SalaryItemCriteria() {
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

    public BooleanFilter getAccounted() {
        return accounted;
    }

    public void setAccounted(BooleanFilter accounted) {
        this.accounted = accounted;
    }

    public LongFilter getMentorId() {
        return mentorId;
    }

    public void setMentorId(LongFilter mentorId) {
        this.mentorId = mentorId;
    }

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LongFilter getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(LongFilter salaryId) {
        this.salaryId = salaryId;
    }

    @Override
    public String toString() {
        return "SalaryItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateFrom != null ? "dateFrom=" + dateFrom + ", " : "") +
            (dateTo != null ? "dateTo=" + dateTo + ", " : "") +
            (accounted != null ? "accounted=" + accounted + ", " : "") +
            (mentorId != null ? "mentorId=" + mentorId + ", " : "") +
            (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
            (salaryId != null ? "salaryId=" + salaryId + ", " : "") +
            "}";
    }

}
