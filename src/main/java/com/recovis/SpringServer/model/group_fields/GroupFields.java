package com.recovis.SpringServer.model.group_fields;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recovis.SpringServer.model.all_fields.AllFields;
import jakarta.persistence.*;

@Entity
public class GroupFields {


    @EmbeddedId
    private GroupFieldsID id;

    @ManyToOne
    @MapsId("field_id")
    @JoinColumn(name = "field_id")
    private AllFields field;

    public AllFields getField() {
        return field;
    }

    public GroupFieldsID getId() {
        return id;
    }

    public void setId(GroupFieldsID id) {
        this.id = id;
    }
}
