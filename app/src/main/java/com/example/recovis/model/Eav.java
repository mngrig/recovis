package com.example.recovis.model;

public class Eav {

    private EavID id;

    private String val;

    public Eav(EavID id, String val) {
        this.id = id;
        this.val = val;
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
}
