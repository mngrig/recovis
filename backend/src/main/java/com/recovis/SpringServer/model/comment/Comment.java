package com.recovis.SpringServer.model.comment;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recovis.SpringServer.model.patient.Patient;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer comment_id;

    @Column(name = "patient_id")
    private String patient_id;

    // insertable = false, updatable = false, because in android client we only have String patient_id in Comment class
    @ManyToOne
    @JoinColumn(name = "patient_id", insertable = false, updatable = false)
    private Patient patient;

    @Nullable
    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date from_date;
    @Nullable
    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date to_date;

    @Nonnull
    private String comment_text;

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Nullable
    public Date getFrom_date() {
        return from_date;
    }

    public void setFrom_date(@Nullable Date from_date) {
        this.from_date = from_date;
    }

    @Nullable
    public Date getTo_date() {
        return to_date;
    }

    public void setTo_date(@Nullable Date to_date) {
        this.to_date = to_date;
    }

    @Nonnull
    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(@Nonnull String comment_text) {
        this.comment_text = comment_text;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }
}
