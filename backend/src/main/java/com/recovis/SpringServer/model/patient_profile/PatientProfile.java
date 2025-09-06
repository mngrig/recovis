package com.recovis.SpringServer.model.patient_profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.recovis.SpringServer.model.all_fields.AllFields;
import com.recovis.SpringServer.model.patient.Patient;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Entity
public class PatientProfile {

    @EmbeddedId
    @JsonIgnore
    private PatientProfileID id;

    @ManyToOne
    @MapsId("patient_id")
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @MapsId("field_id")
    @JoinColumn(name = "field_id")
    private AllFields field;

    @NonNull
    private Integer required;
    @Nullable
    private String guideline;


    public PatientProfile() {

    }

    public PatientProfile(Patient patient, AllFields field, @NonNull Integer required, @Nullable String guideline){
        this.id = new PatientProfileID(patient.getPatient_id(),field.getField_id());
        this.patient = patient;
        this.field = field;
        this.required = required;
        this.guideline = guideline;
    }

    public PatientProfileID getId() {
        return id;
    }

    public void setId(PatientProfileID id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public AllFields getField() {
        return field;
    }

    public void setField(AllFields field) {
        this.field = field;
    }

    @NonNull
    public Integer getRequired() {
        return required;
    }

    public void setRequired(@NonNull Integer required) {
        this.required = required;
    }

    @Nullable
    public String getGuideline() {
        return guideline;
    }

    public void setGuideline(@Nullable String guideline) {
        this.guideline = guideline;
    }
}
