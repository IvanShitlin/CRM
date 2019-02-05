package com.foxminded.hipsterfox.service.dto;

import com.foxminded.hipsterfox.domain.enumeration.CloseType;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;

import java.io.Serializable;

/**
 * Criteria class for the Contract entity. This class is used in ContractResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contracts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContractCriteria implements Serializable {
    /**
     * Class for filtering CloseType
     */
    public static class CloseTypeFilter extends Filter<CloseType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LocalDateFilter firstPayDate;

    private LocalDateFilter nextPayDate;

    private CloseTypeFilter closeType;

    private LongFilter mentorId;

    private LongFilter agreementId;

    private MoneyCriteria price;

    private MoneyCriteria mentorRate;

    public ContractCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LocalDateFilter getFirstPayDate() {
        return firstPayDate;
    }

    public void setFirstPayDate(LocalDateFilter firstPayDate) {
        this.firstPayDate = firstPayDate;
    }

    public LocalDateFilter getNextPayDate() {
        return nextPayDate;
    }

    public void setNextPayDate(LocalDateFilter nextPayDate) {
        this.nextPayDate = nextPayDate;
    }

    public CloseTypeFilter getCloseType() {
        return closeType;
    }

    public void setCloseType(CloseTypeFilter closeType) {
        this.closeType = closeType;
    }

    public LongFilter getMentorId() {
        return mentorId;
    }

    public void setMentorId(LongFilter mentorId) {
        this.mentorId = mentorId;
    }

    public LongFilter getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(LongFilter agreementId) {
        this.agreementId = agreementId;
    }

    public MoneyCriteria getPrice() {
        return price;
    }

    public void setPrice(MoneyCriteria price) {
        this.price = price;
    }

    public MoneyCriteria getMentorRate() {
        return mentorRate;
    }

    public void setMentorRate(MoneyCriteria mentorRate) {
        this.mentorRate = mentorRate;
    }

    @Override
    public String toString() {
        return "ContractCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (firstPayDate != null ? "firstPayDate=" + firstPayDate + ", " : "") +
            (nextPayDate != null ? "nextPayDate=" + nextPayDate + ", " : "") +
            (closeType != null ? "closeType=" + closeType + ", " : "") +
            (mentorId != null ? "mentorId=" + mentorId + ", " : "") +
            (agreementId != null ? "agreementId=" + agreementId + ", " : "") +
            (price != null ? price.toString("price") : "") +
            (mentorRate != null ? mentorRate.toString("mentorRate") : "") +
            "}";
    }

}
