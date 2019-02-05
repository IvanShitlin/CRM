package com.foxminded.hipsterfox.domain;

import com.foxminded.hipsterfox.domain.enumeration.AmocrmActionType;
import com.foxminded.hipsterfox.domain.enumeration.AmocrmEntitytype;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "amocrm_info")
public class AmocrmInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amocrmInfoGenerator")
    @SequenceGenerator(name = "amocrmInfoGenerator", sequenceName = "amocrm_info_sequence")
    private Long id;

    @NotNull
    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @NotNull
    @Column(name = "imported_entity_amount")
    private Integer importedEntitiesAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private AmocrmEntitytype entityType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private AmocrmActionType actionType;

    public AmocrmInfo() {
    }

    public AmocrmInfo(@NotNull ZonedDateTime dateTime, @NotNull Integer importedEntitiesAmount,
                      @NotNull AmocrmEntitytype entityType, @NotNull AmocrmActionType actionType) {
        this.dateTime = dateTime;
        this.importedEntitiesAmount = importedEntitiesAmount;
        this.entityType = entityType;
        this.actionType = actionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getImportedEntitiesAmount() {
        return importedEntitiesAmount;
    }

    public void setImportedEntitiesAmount(Integer importedEntitiesAmount) {
        this.importedEntitiesAmount = importedEntitiesAmount;
    }

    public AmocrmEntitytype getEntityType() {
        return entityType;
    }

    public void setEntityType(AmocrmEntitytype entityType) {
        this.entityType = entityType;
    }

    public AmocrmActionType getActionType() {
        return actionType;
    }

    public void setActionType(AmocrmActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmocrmInfo that = (AmocrmInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AmocrmInfo{" +
            "id=" + id +
            ", dateTime=" + dateTime +
            ", importedEntitiesAmount=" + importedEntitiesAmount +
            ", entityType=" + entityType +
            ", actionType=" + actionType +
            '}';
    }
}
