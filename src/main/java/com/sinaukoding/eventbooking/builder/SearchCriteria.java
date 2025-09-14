package com.sinaukoding.eventbooking.builder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class SearchCriteria implements Serializable {

    public static final String OPERATOR_OR = "OR";
    public static final String OPERATOR_AND = "AND";

    private String key;
    private String operation;
    private Object value;

    public static SearchCriteria of(String key, String operation, Object value) {
        return SearchCriteria.builder()
                .key(key)
                .operation(operation)
                .value(value)
                .build();
    }

    public static SearchCriteria of(String key, String operation) {
        return SearchCriteria.builder()
                .key(key)
                .operation(operation)
                .value(null)
                .build();
    }

}
