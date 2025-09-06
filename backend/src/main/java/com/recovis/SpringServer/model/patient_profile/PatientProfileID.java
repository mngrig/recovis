package com.recovis.SpringServer.model.patient_profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.recovis.SpringServer.model.all_fields.AllFields;
import com.recovis.SpringServer.model.patient.Patient;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class PatientProfileID implements Serializable {

    private String patient_id;

    private Integer field_id;

    public PatientProfileID() {

    }

    public PatientProfileID(String patient_id, Integer field_id){
        this.patient_id = patient_id;
        this.field_id = field_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public Integer getField_id() {
        return field_id;
    }

    public void setField_id(Integer field_id) {
        this.field_id = field_id;
    }
}


