package com.example.recovis.model;

import java.util.Date;

public class Comment {

    private Integer comment_id;

    private String patient_id;

    private Date from_date;

    private Date to_date;

    private String comment_text;

    public Comment(String patient_id, Date from_date, Date to_date, String comment_text) {
        this.patient_id = patient_id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.comment_text = comment_text;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public Date getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Date from_date) {
        this.from_date = from_date;
    }

    public Date getTo_date() {
        return to_date;
    }

    public void setTo_date(Date to_date) {
        this.to_date = to_date;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }
}
