package com.recovis.SpringServer.model.eav;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recovis.SpringServer.model.all_fields.AllFields;
import com.recovis.SpringServer.model.patient.Patient;
import jakarta.persistence.*;

@Entity
public class Eav {

    @EmbeddedId
    private EavID id;

    private String val;

    @ManyToOne
    @MapsId("patient_id")
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @MapsId("field_id")
    @JoinColumn(name = "field_id")
    private AllFields field;

    public Eav(){

    }

    public Eav(EavID id, String val, Patient patient, AllFields field) {
        this.id = id;
        this.val = val;
        this.patient = patient;
        this.field = field;
    }

    public EavID getId() {
        return id;
    }

    public void setId(EavID id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
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
}
