package com.recovis.SpringServer.model.yearly_fields;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recovis.SpringServer.model.patient.Patient;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.java.YearJavaType;

import java.io.Serializable;
import java.util.Date;
import java.time.Year;
import java.util.Optional;

@Embeddable
public class YearlyFieldsID implements Serializable {

    private String patient_id;

    private int yeardate;

    public YearlyFieldsID() {
    }

    public YearlyFieldsID(String patient_id, int yeardate) {
        this.patient_id = patient_id;
        this.yeardate = yeardate;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public int getYeardate() {
        return yeardate;
    }

    public void setYeardate(int yeardate) {
        this.yeardate = yeardate;
    }
}
