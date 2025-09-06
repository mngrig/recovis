package com.example.recovis.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class EavID implements Serializable {
    private String patient_id;

    private Date exam_date;

    private Integer field_id;

    public EavID(String patient_id, Date exam_date, Integer field_id) {
        this.patient_id = patient_id;
        this.exam_date = exam_date;
        this.field_id = field_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public Date getExam_date() {
        return exam_date;
    }

    public void setExam_date(Date exam_date) {
        this.exam_date = exam_date;
    }

    public Integer getField_id() {
        return field_id;
    }

    public void setField_id(Integer field_id) {
        this.field_id = field_id;
    }
}
