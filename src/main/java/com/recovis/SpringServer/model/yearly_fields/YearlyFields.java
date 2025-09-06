package com.recovis.SpringServer.model.yearly_fields;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.recovis.SpringServer.model.patient.Patient;
import jakarta.persistence.*;
import org.hibernate.type.descriptor.java.YearJavaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class YearlyFields {

    @EmbeddedId
    private YearlyFieldsID id;

    @NonNull
    private int heart_triplex;
    @NonNull
    private int abdominal_us;
    @NonNull
    private int dxa;


    @ManyToOne
    @MapsId("patient_id")
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;


    public YearlyFields(){

    }

    public YearlyFields(YearlyFieldsID id, int heart_triplex, int abdominal_us, int dxa, Patient patient) {
        this.id = id;
        this.heart_triplex = heart_triplex;
        this.abdominal_us = abdominal_us;
        this.dxa = dxa;
        this.patient = patient;
    }

    public YearlyFieldsID getId() {
        return id;
    }

    public void setId(YearlyFieldsID id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public int getHeart_triplex() {
        return heart_triplex;
    }

    public void setHeart_triplex(int heart_triplex) {
        this.heart_triplex = heart_triplex;
    }

    public int getAbdominal_us() {
        return abdominal_us;
    }

    public void setAbdominal_us(int abdominal_us) {
        this.abdominal_us = abdominal_us;
    }

    public int getDxa() {
        return dxa;
    }

    public void setDxa(int dxa) {
        this.dxa = dxa;
    }

    @Override
    public String toString() {
        return "YearlyFields{" +
                "id=" + id +
                ", patient=" + patient +
                ", heart_triplex=" + heart_triplex +
                ", abdominal_us=" + abdominal_us +
                ", dxa=" + dxa +
                '}';
    }
}
