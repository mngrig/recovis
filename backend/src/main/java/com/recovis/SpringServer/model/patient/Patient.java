package com.recovis.SpringServer.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recovis.SpringServer.model.all_fields.AllFields;
import com.recovis.SpringServer.model.comment.Comment;
import com.recovis.SpringServer.model.eav.Eav;
import com.recovis.SpringServer.model.patient_profile.PatientProfile;
import com.recovis.SpringServer.model.patient_profile.PatientProfileID;
import com.recovis.SpringServer.model.yearly_fields.YearlyFields;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Patient {

  @Id
  private String patient_id;

  @OneToMany(mappedBy = "patient")
  private Set<PatientProfile> patientProfiles = new HashSet<>();

  @OneToMany(mappedBy = "patient")
  private Set<YearlyFields> yearlyFields = new HashSet<>();

  @OneToMany(mappedBy = "patient")
  private Set<Eav> eavSet = new HashSet<>();

  @NonNull
  private String first_name;
  @NonNull
  private String second_name;
  @Nullable
  private Date date_of_birth;
  @Nullable
  private String tel;
  @Nullable
  private String email;
  @Nullable
  private Date last_transplant_date;
  @Nullable
  private String transplant_type;
  @Nullable
  private Integer transplants_num;
  @Nullable
  private String kidney_failure_cause;
  @Column(unique = true)
  @NonNull
  private String username;
  @NonNull
  private String userpassword;

  public Patient() {

  }

  public Patient(String patient_id,@NonNull String first_name, @NonNull String second_name, @Nullable Date date_of_birth, @Nullable String tel, @Nullable String email, @Nullable Date last_transplant_date, @Nullable String transplant_type, @Nullable Integer transplants_num, @Nullable String kidney_failure_cause, @NonNull String username, @NonNull String userpassword) {
    this.patient_id = patient_id;
    this.first_name = first_name;
    this.second_name = second_name;
    this.date_of_birth = date_of_birth;
    this.tel = tel;
    this.email = email;
    this.last_transplant_date = last_transplant_date;
    this.transplant_type = transplant_type;
    this.transplants_num = transplants_num;
    this.kidney_failure_cause = kidney_failure_cause;
    this.username = username;
    this.userpassword = userpassword;
  }

  public String getPatient_id() {
    return patient_id;
  }

  public void setPatient_id(String patient_id) {
    this.patient_id = patient_id;
  }

  @NonNull
  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(@NonNull String first_name) {
    this.first_name = first_name;
  }

  @NonNull
  public String getSecond_name() {
    return second_name;
  }

  public void setSecond_name(@NonNull String second_name) {
    this.second_name = second_name;
  }

  @Nullable
  public Date getDate_of_birth() {
    return date_of_birth;
  }

  public void setDate_of_birth(@Nullable Date date_of_birth) {
    this.date_of_birth = date_of_birth;
  }

  @Nullable
  public String getTel() {
    return tel;
  }

  public void setTel(@Nullable String tel) {
    this.tel = tel;
  }

  @Nullable
  public String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  @Nullable
  public Date getLast_transplant_date() {
    return last_transplant_date;
  }

  public void setLast_transplant_date(@Nullable Date last_transplant_date) {
    this.last_transplant_date = last_transplant_date;
  }

  @Nullable
  public String getTransplant_type() {
    return transplant_type;
  }

  public void setTransplant_type(@Nullable String transplant_type) {
    this.transplant_type = transplant_type;
  }

  @Nullable
  public Integer getTransplants_num() {
    return transplants_num;
  }

  public void setTransplants_num(@Nullable Integer transplants_num) {
    this.transplants_num = transplants_num;
  }

  @Nullable
  public String getKidney_failure_cause() {
    return kidney_failure_cause;
  }

  public void setKidney_failure_cause(@Nullable String kidney_failure_cause) {
    this.kidney_failure_cause = kidney_failure_cause;
  }

  @NonNull
  public String getUsername() {
    return username;
  }

  public void setUsername(@NonNull String username) {
    this.username = username;
  }

  @NonNull
  public String getUserpassword() {
    return userpassword;
  }

  public void setUserpassword(@NonNull String userpassword) {
    this.userpassword = userpassword;
  }
}
