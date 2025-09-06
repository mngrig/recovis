package com.example.recovis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "patient_id",
        "yeardate"
})
public class YearlyFieldsID {

    @JsonProperty("patient_id")
    public String patient_id;
    @JsonProperty("yeardate")
    public Integer yeardate;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public Integer getYeardate() {
        return yeardate;
    }

    public void setYeardate(Integer yeardate) {
        this.yeardate = yeardate;
    }
}