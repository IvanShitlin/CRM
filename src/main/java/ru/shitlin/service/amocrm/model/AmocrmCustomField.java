package ru.shitlin.service.amocrm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmocrmCustomField {

    private Long id;
    private String name;
    private List<AmocrmCustomFieldValue> values;
    @JsonProperty("is_system")
    private Boolean isSystem;

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

    public List<AmocrmCustomFieldValue> getValues() {
        return values;
    }

    public void setValues(List<AmocrmCustomFieldValue> values) {
        this.values = values;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmocrmCustomField that = (AmocrmCustomField) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AmocrmCustomField{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", isSystem=" + isSystem +
            '}';
    }
}
