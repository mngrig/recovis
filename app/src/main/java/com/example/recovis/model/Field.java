package com.example.recovis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "field_id",
        "description_gr",
        "description_en",
        "frequency",
        "measurement_unit",
        "acceptable_range",
})
public class Field implements Serializable {

    @JsonProperty("field_id")
    public Integer field_id;
    @JsonProperty("description_gr")
    public String description_gr;
    @JsonProperty("description_en")
    public String description_en;
    @JsonProperty("frequency")
    public String frequency;
    @JsonProperty("measurement_unit")
    public String measurement_unit;
    @JsonProperty("acceptable_range")
    public String acceptable_range;

    public Integer getField_id() {
        return field_id;
    }

    public void setField_id(Integer field_id) {
        this.field_id = field_id;
    }

    public String getDescription_gr() {
        return description_gr;
    }

    public void setDescription_gr(String description_gr) {
        this.description_gr = description_gr;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getMeasurement_unit() {
        return measurement_unit;
    }

    public void setMeasurement_unit(String measurement_unit) {
        this.measurement_unit = measurement_unit;
    }

    public String getAcceptable_range() {
        return acceptable_range;
    }

    public void setAcceptable_range(String acceptable_range) {
        this.acceptable_range = acceptable_range;
    }
}
