package com.sinaukoding.eventbooking.model.enums;


import lombok.Getter;

@Getter
public enum Status {
    AKTIF("Aktif"),
    TIDAK_AKTIF("Tidak Aktif");

    private final String label;

    Status(String label) {
        this.label = label;
    }
}
