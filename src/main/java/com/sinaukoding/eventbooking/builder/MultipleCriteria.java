package com.sinaukoding.eventbooking.builder;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class MultipleCriteria implements Serializable {

    private MultipleCriteria inner;
    private List<Criteria> criterias;
    private String operatorCriteria;
    private String operatorInner;
    private Boolean isProcessed;

    public MultipleCriteria() {
    }

    public MultipleCriteria(MultipleCriteria builder) {
        this.inner = builder.getInner();
        this.criterias = builder.getCriterias();
        this.operatorInner = builder.getOperatorInner();
        this.operatorCriteria = builder.getOperatorCriteria();
        this.isProcessed = builder.getIsProcessed();
    }

    public static MultipleCriteria builder() {
        MultipleCriteria multipleCriteria = new MultipleCriteria();
        multipleCriteria.setInner(null);
        multipleCriteria.setCriterias(new ArrayList<>());
        multipleCriteria.setOperatorCriteria(SearchCriteria.OPERATOR_AND);
        multipleCriteria.setOperatorInner(SearchCriteria.OPERATOR_AND);
        multipleCriteria.setIsProcessed(false);
        return multipleCriteria;
    }

    public MultipleCriteria inner(MultipleCriteria inner) {
        this.inner = inner;
        return this;
    }

    public MultipleCriteria criterias(Criteria... criteria) {
        this.criterias = Arrays.asList(criteria);
        return this;
    }

    public MultipleCriteria criterias(List<Criteria> criterias) {
        this.criterias = criterias;
        return this;
    }

    public MultipleCriteria criterias(SearchCriteria... searchCriteria) {
        this.criterias = Collections.singletonList(Criteria.builder()
                .searchCriterias(searchCriteria)
                .build());
        return this;
    }

    public MultipleCriteria criterias(String operator, SearchCriteria... searchCriteria) {
        this.criterias = Collections.singletonList(Criteria.builder()
                .searchCriterias(searchCriteria)
                .operator(operator)
                .build());
        return this;
    }

    public MultipleCriteria operatorCriteria(String operatorCriteria) {
        this.operatorCriteria = operatorCriteria;
        return this;
    }

    public MultipleCriteria operatorInner(String operatorInner) {
        this.operatorInner = operatorInner;
        return this;
    }

    public MultipleCriteria isProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
        return this;
    }

    public MultipleCriteria build() {
        return new MultipleCriteria(this);
    }

    @Getter
    @Setter
    public static class Criteria implements Serializable {
        private List<SearchCriteria> searchCriterias;
        private String operator;

        public Criteria() {
        }

        public Criteria(Criteria builder) {
            this.searchCriterias = builder.getSearchCriterias();
            this.operator = builder.getOperator();
        }

        public static Criteria builder() {
            Criteria criteria = new Criteria();
            criteria.setSearchCriterias(new ArrayList<>());
            criteria.setOperator(SearchCriteria.OPERATOR_AND);
            return criteria;
        }

        public Criteria searchCriterias(SearchCriteria... searchCriteria) {
            this.searchCriterias = Arrays.asList(searchCriteria);
            return this;
        }

        public Criteria searchCriterias(List<SearchCriteria> searchCriterias) {
            this.searchCriterias = searchCriterias;
            return this;
        }

        public Criteria operator(String operator) {
            this.operator = operator;
            return this;
        }

        public Criteria build() {
            return new Criteria(this);
        }
    }

}
