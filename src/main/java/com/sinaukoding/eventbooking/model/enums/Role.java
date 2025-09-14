package com.sinaukoding.eventbooking.model.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    ORGANIZER("ORGANIZER");

    private final String label;

    Role(String label) {
        this.label = label;
    }
}
