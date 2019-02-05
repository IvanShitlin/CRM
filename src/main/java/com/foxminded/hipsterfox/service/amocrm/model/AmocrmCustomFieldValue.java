package com.foxminded.hipsterfox.service.amocrm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmocrmCustomFieldValue {
    private String value;
    @JsonProperty("enum")
    private Long enumId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getEnumId() {
        return enumId;
    }

    public void setEnumId(Long enumId) {
        this.enumId = enumId;
    }

    @Override
    public String toString() {
        return "AmocrmCustomFieldValue{" +
            "value='" + value + '\'' +
            ", enumId=" + enumId +
            '}';
    }
}
