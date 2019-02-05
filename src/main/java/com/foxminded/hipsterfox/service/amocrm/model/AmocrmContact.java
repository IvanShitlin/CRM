package com.foxminded.hipsterfox.service.amocrm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.core.Relation;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(collectionRelation = "items")
public class AmocrmContact {

    private Long id;
    private String name;
    @JsonProperty("responsible_user_id")
    private String responsibleUserId;
    @JsonProperty("created_by")
    private Long createdBy;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("updated_at")
    private Long updatedAt;
    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("updated_by")
    private Long updatedBy;
    @JsonProperty("group_id")
    private Long groupId;
    private AmocrmCompany company;
    private AmocrmLead leads;
    @JsonProperty("closest_task_at")
    private Long closest_task_at;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<AmocrmTags> tags;
    @JsonProperty("custom_fields")
    private List<AmocrmCustomField> customFields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponsibleUserId() {
        return responsibleUserId;
    }

    public void setResponsibleUserId(String responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public AmocrmCompany getCompany() {
        return company;
    }

    public void setCompany(AmocrmCompany company) {
        this.company = company;
    }

    public AmocrmLead getLeads() {
        return leads;
    }

    public void setLeads(AmocrmLead leads) {
        this.leads = leads;
    }

    public Long getClosest_task_at() {
        return closest_task_at;
    }

    public void setClosest_task_at(Long closest_task_at) {
        this.closest_task_at = closest_task_at;
    }

    public List<AmocrmTags> getTags() {
        return tags;
    }

    public void setTags(List<AmocrmTags> tags) {
        this.tags = tags;
    }

    public List<AmocrmCustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<AmocrmCustomField> customFields) {
        this.customFields = customFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmocrmContact that = (AmocrmContact) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AmocrmContact{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", responsibleUserId='" + responsibleUserId + '\'' +
            ", createdBy=" + createdBy +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", accountId=" + accountId +
            ", updatedBy=" + updatedBy +
            ", groupId=" + groupId +
            ", company=" + company +
            ", leads=" + leads +
            ", closest_task_at=" + closest_task_at +
            ", tags=" + tags +
            ", customFields=" + customFields +
            '}';
    }
}
