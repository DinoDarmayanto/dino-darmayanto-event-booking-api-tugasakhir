package com.sinaukoding.eventbooking.builder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specification.*;

public class CustomBuilder<T> {

    private final List<Helper> helpers;

    public CustomBuilder() {
        helpers = new ArrayList<>();
    }

    public CustomBuilder<T> with(String key, String operation, Object value) {
        helpers.add(Helper.builder()
                .criteria(SearchCriteria.of(key, operation, value))
                .build());
        return this;
    }

    public CustomBuilder<T> withNull(String key) {
        helpers.add(Helper.builder()
                .criteria(SearchCriteria.of(key, CustomSpecification.OPERATION_NULL, null))
                .build());
        return this;
    }

    public CustomBuilder<T> withJoinNull(String key) {
        helpers.add(Helper.builder()
                .criteria(SearchCriteria.of(key, CustomSpecification.OPERATION_JOIN_NULL, null))
                .build());
        return this;
    }

    public CustomBuilder<T> withNotNull(String key) {
        helpers.add(Helper.builder()
                .criteria(SearchCriteria.of(key, CustomSpecification.OPERATION_NOT_NULL, "null"))
                .build());
        return this;
    }

    public CustomBuilder<T> with(SearchCriteria criteria) {
        helpers.add(Helper.builder()
                .criteria(criteria)
                .build());
        return this;
    }

    public CustomBuilder<T> with(SearchCriteria criteria, String operator) {
        helpers.add(Helper.builder()
                .criteria(criteria)
                .operator(operator)
                .build());
        return this;
    }

    public CustomBuilder<T> with(MultipleCriteria multipleCriteria) {
        helpers.add(Helper.builder()
                .multipleCriteria(multipleCriteria)
                .build());
        return this;
    }

    public CustomBuilder<T> with(MultipleCriteria multipleCriteria, String operator) {
        helpers.add(Helper.builder()
                .multipleCriteria(multipleCriteria)
                .operator(operator)
                .build());
        return this;
    }

    public Specification<T> build() {
        if (helpers.isEmpty()) return null;

        List<Specification<T>> specs = new ArrayList<>();
        Collections.reverse(helpers);

        helpers.forEach(helper -> {
            if (helper.getCriteria() != null) {
                specs.add(new CustomSpecification<>(helper.getCriteria()));
            } else if (helper.getMultipleCriteria() != null) {
                specs.add(new CustomSpecification<>(helper.getMultipleCriteria()));
            }
        });

        Specification<T> result = null;

        int specsI = 0;
        for (Helper helper : helpers) {
            if (specsI == 0) {
                result = specs.get(specsI);
                specsI++;
                continue;
            }

            if (StringUtils.isNotEmpty(helper.getOperator()) && helper.getOperator().equals(SearchCriteria.OPERATOR_OR)) {
                result = Objects.requireNonNull(anyOf(result)).or(specs.get(specsI));
            } else {
                result = Objects.requireNonNull(allOf(result)).and(specs.get(specsI));
            }

            specsI++;
        }

        return result;
    }

    @Getter
    @Setter
    @Builder
    private static class Helper {
        private SearchCriteria criteria;
        private MultipleCriteria multipleCriteria;
        private String operator;
    }

}
