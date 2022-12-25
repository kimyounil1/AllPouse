package com.perfume.allpouse.model.enums;

public enum Permission {

    ADMIN("ROLE_ADMIN"),
    ROLE_PERFUMER("ROLE_PERFUMER"),
    ROLE_USER("ROLE_USER");
    private String value;
    Permission(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
