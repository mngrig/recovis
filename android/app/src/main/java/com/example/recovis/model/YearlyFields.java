package com.example.recovis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "heart_triplex",
        "abdominal_us",
        "dxa"
})
public class YearlyFields {

    @JsonProperty("id")
    public YearlyFieldsID id;
    @JsonProperty("heart_triplex")
    public Integer heart_triplex;
    @JsonProperty("abdominal_us")
    public Integer abdominal_us;
    @JsonProperty("dxa")
    public Integer dxa;

    public YearlyFieldsID getYearlyFieldsID() {
        return id;
    }

    public void setYearlyFieldsID(YearlyFieldsID yearlyFieldsID) {
        this.id = yearlyFieldsID;
    }

    public Integer getHeart_triplex() {
        return heart_triplex;
    }

    public void setHeart_triplex(Integer heart_triplex) {
        this.heart_triplex = heart_triplex;
    }

    public Integer getAbdominal_us() {
        return abdominal_us;
    }

    public void setAbdominal_us(Integer abdominal_us) {
        this.abdominal_us = abdominal_us;
    }

    public Integer getDxa() {
        return dxa;
    }

    public void setDxa(Integer dxa) {
        this.dxa = dxa;
    }
}
