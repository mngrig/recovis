package com.recovis.SpringServer.model.all_fields;

import com.recovis.SpringServer.model.eav.Eav;
import com.recovis.SpringServer.model.patient_profile.PatientProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.UniqueConstraint;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class AllFields {

    @Id
    private int field_id;

    @OneToMany(mappedBy = "field")
    private Set<PatientProfile> patientProfiles = new HashSet<>();

    @OneToMany(mappedBy = "field")
    private Set<Eav> eavSet = new HashSet<>();

    @NonNull
    private String description_gr;
    @NonNull
    private String description_en;
    @NonNull
    private String frequency;
    @NonNull
    private String measurement_unit;
    @NonNull
    private String acceptable_range;

    private String type;

    @Nullable
    private String expression;

    @Nullable
    private Integer order_id;

    public AllFields() {
    }

    public AllFields(int field_id, @NonNull String description_gr, @NonNull String description_en, @NonNull String frequency, @NonNull String measurement_unit, @NonNull String acceptable_range, @Nullable Integer order_id) {
        this.field_id = field_id;
        this.description_gr = description_gr;
        this.description_en = description_en;
        this.frequency = frequency;
        this.measurement_unit = measurement_unit;
        this.acceptable_range = acceptable_range;
        this.order_id = order_id;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    @NonNull
    public String getDescription_gr() {
        return description_gr;
    }

    public void setDescription_gr(@NonNull String description_gr) {
        this.description_gr = description_gr;
    }

    @NonNull
    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(@NonNull String description_en) {
        this.description_en = description_en;
    }

    @NonNull
    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(@NonNull String frequency) {
        this.frequency = frequency;
    }

    @NonNull
    public String getMeasurement_unit() {
        return measurement_unit;
    }

    public void setMeasurement_unit(@NonNull String measurement_unit) {
        this.measurement_unit = measurement_unit;
    }

    @NonNull
    public String getAcceptable_range() {
        return acceptable_range;
    }

    public void setAcceptable_range(@NonNull String acceptable_range) {
        this.acceptable_range = acceptable_range;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @Nullable
    public String getExpression() {
        return expression;
    }

    public void setExpression(@Nullable String expression) {
        this.expression = expression;
    }

    @Nullable
    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }


}
