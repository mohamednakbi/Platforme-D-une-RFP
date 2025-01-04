package com.satoripop.rfp.service.keycloak.dto;

import java.io.Serializable;

public class Credentials implements Serializable {

    private String type;
    private String value;
    private boolean temporary;

    public Credentials(String value) {
        this.type = "password";
        this.value = value;
    }

    public Credentials(String type, String value, boolean temporary) {
        this.type = type;
        this.value = value;
        this.temporary = temporary;
    }

    public Credentials() {}

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
