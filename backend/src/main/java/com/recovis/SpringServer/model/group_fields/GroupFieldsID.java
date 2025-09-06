package com.recovis.SpringServer.model.group_fields;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class GroupFieldsID implements Serializable {

    private Integer group_id;

    private Integer field_id;

    public GroupFieldsID() {

    };

    public GroupFieldsID(Integer group_id, Integer field_id) {
        this.group_id = group_id;
        this.field_id = field_id;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Integer getField_id() {
        return field_id;
    }

    public void setField_id(Integer field_id) {
        this.field_id = field_id;
    }
}
