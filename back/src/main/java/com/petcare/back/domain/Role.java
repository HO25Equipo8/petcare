package com.petcare.back.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    OWNER("ROLE_OWNER"),
    SITTER("ROLE_SITTER"),
    USER("ROLE_USER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
