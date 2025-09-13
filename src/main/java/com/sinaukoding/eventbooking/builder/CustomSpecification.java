package com.sinaukoding.eventbooking.builder;

import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CustomSpecification<T> implements Specification<T> {

    public static final String OPERATION_LIKE = "LIKE";
    public static final String OPERATION_EQUAL = "EQUAL";
    public static final String OPERATION_NOT_EQUAL = "NOT_EQUAL";
    public static final String OPERATION_EQUAL_STRING = "EQUAL_STRING";
    public static final String OPERATION_LOCAL_DATE_BETWEEN = "LOCAL_DATE_BETWEEN";
    public static final String OPERATION_LOCAL_DATE_TIME_BETWEEN = "LOCAL_DATE_TIME_BETWEEN";
    public static final String OPERATION_LOCAL_DATE_TIME_GREATER_THAN_EQUAL = "LOCAL_DATE_TIME_GREATER_THAN_EQUAL";
    public static final String OPERATION_LOCAL_DATE_GREATER_THAN = "LOCAL_DATE_GREATER_THAN";
    public static final String OPERATION_LOCAL_DATE_GREATER_THAN_EQUAL = "LOCAL_DATE_GREATER_THAN_EQUAL";
    public static final String OPERATION_LOCAL_DATE_LESS_THAN = "LOCAL_DATE_LESS_THAN";
    public static final String OPERATION_LOCAL_DATE_LESS_THAN_EQUAL = "LOCAL_DATE_LESS_THAN_EQUAL";
    public static final String OPERATION_GREATER_THAN = "GREATER_THAN";
    public static final String OPERATION_GREATER_THAN_EQUAL = "GREATER_THAN_EQUAL";
    public static final String OPERATION_LESS_THAN = "LESS_THAN";
    public static final String OPERATION_LESS_THAN_EQUAL = "LESS_THAN_EQUAL";
    public static final String OPERATION_IN = "IN";
    public static final String OPERATION_NOT_IN = "NOT_IN";
    public static final String OPERATION_JOIN_LIKE = "JOIN_LIKE";
    public static final String OPERATION_JOIN_EQUAL = "JOIN_EQUAL";
    public static final String OPERATION_JOIN_NOT_EQUAL = "JOIN_NOT_EQUAL";
    public static final String OPERATION_JOIN_EQUAL_STRING = "JOIN_EQUAL_STRING";
    public static final String OPERATION_JOIN_IN = "JOIN_IN";
    public static final String OPERATION_NULL = "NULL";
    public static final String OPERATION_JOIN_NULL = "JOIN_NULL";
    public static final String OPERATION_JOIN_EMPTY = "JOIN_EMPTY";
    public static final String OPERATION_NOT_NULL = "NOT_NULL";
    public static final String OPERATION_JOIN_NOT_NULL = "JOIN_NOT_NULL";
    public static final String OPERATION_JOIN_NOT_EMPTY = "JOIN_NOT_EMPTY";
    public static final String OPERATION_YEAR = "YEAR";
    public static final String OPERATION_JOIN_YEAR = "JOIN_YEAR";

    private static final String SEPARATOR = "\\.";
    private final SearchCriteria searchCriteria;
    private final MultipleCriteria multipleCriteria;
    private final DateTimeFormatter formatLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter formatLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
        this.multipleCriteria = null;
    }

    public CustomSpecification(MultipleCriteria multipleCriteria) {
        this.searchCriteria = null;
        this.multipleCriteria = multipleCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        criteriaQuery.distinct(true);
        if (searchCriteria != null) {
            return getPredicate(searchCriteria, root, builder);
        }

        if (multipleCriteria != null) {
            MultipleCriteria multipleCriteria = SerializationUtils.clone(this.multipleCriteria);
            return getPredicate(multipleCriteria, root, builder);
        }

        return null;
    }

    private Predicate getPredicate(SearchCriteria searchCriteria, Root<T> root, CriteriaBuilder builder) {
        switch (searchCriteria.getOperation().toUpperCase()) {
            case OPERATION_LIKE: {
                String value = "%" + searchCriteria.getValue().toString().toLowerCase() + "%";
                return builder.like(builder.lower(getPath(root, searchCriteria).as(String.class)), value);
            }

            case OPERATION_EQUAL: {
                return builder.equal(getPath(root, searchCriteria), searchCriteria.getValue());
            }

            case OPERATION_NOT_EQUAL: {
                return builder.notEqual(getPath(root, searchCriteria), searchCriteria.getValue());
            }

            case OPERATION_EQUAL_STRING: {
                return builder.equal(getPath(root, searchCriteria).as(String.class), searchCriteria.getValue());
            }

            case OPERATION_NULL: {
                return builder.isNull(getPath(root, searchCriteria));
            }

            case OPERATION_NOT_NULL: {
                return builder.isNotNull(getPath(root, searchCriteria));
            }

            case OPERATION_LOCAL_DATE_BETWEEN: {
                String[] dateStr = searchCriteria.getValue().toString().split(" - ");
                LocalDate dateStart = LocalDate.parse(dateStr[0], formatLocalDate);
                LocalDate dateEnd = LocalDate.parse(dateStr[1], formatLocalDate);

                return builder.between((Expression) getPath(root, searchCriteria), dateStart, dateEnd);
            }

            case OPERATION_LOCAL_DATE_TIME_BETWEEN: {
                String[] dateStr = searchCriteria.getValue().toString().split(" - ");
                LocalDateTime dateStart = LocalDateTime.parse(dateStr[0], formatLocalDateTime);
                LocalDateTime dateEnd = LocalDateTime.parse(dateStr[1], formatLocalDateTime);

                return builder.between((Expression) getPath(root, searchCriteria), dateStart, dateEnd);
            }

            case OPERATION_LOCAL_DATE_TIME_GREATER_THAN_EQUAL: {
                LocalDateTime dateTime = (LocalDateTime) searchCriteria.getValue();
                return builder.greaterThanOrEqualTo(getPath(root, searchCriteria).as(LocalDateTime.class), dateTime);
            }

            case OPERATION_LOCAL_DATE_GREATER_THAN: {
                LocalDate date = LocalDate.parse(searchCriteria.getValue().toString(), formatLocalDate);
                return builder.greaterThan(getPath(root, searchCriteria).as(LocalDate.class), date);
            }

            case OPERATION_LOCAL_DATE_GREATER_THAN_EQUAL: {
                LocalDate date = LocalDate.parse(searchCriteria.getValue().toString(), formatLocalDate);
                return builder.greaterThanOrEqualTo(getPath(root, searchCriteria).as(LocalDate.class), date);
            }

            case OPERATION_LOCAL_DATE_LESS_THAN: {
                LocalDate date = LocalDate.parse(searchCriteria.getValue().toString(), formatLocalDate);
                return builder.lessThan(getPath(root, searchCriteria).as(LocalDate.class), date);
            }

            case OPERATION_LOCAL_DATE_LESS_THAN_EQUAL: {
                LocalDate date = LocalDate.parse(searchCriteria.getValue().toString(), formatLocalDate);
                return builder.lessThanOrEqualTo(getPath(root, searchCriteria).as(LocalDate.class), date);
            }

            case OPERATION_IN: {
                return builder.in(root.get(searchCriteria.getKey())).value(searchCriteria.getValue());
            }

            case OPERATION_NOT_IN: {
                return builder.not(builder.in(root.get(searchCriteria.getKey())).value(searchCriteria.getValue()));
            }

            case OPERATION_JOIN_EQUAL: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.equal(getJoin(root, searchCriteria).get(fields[fields.length - 1]), searchCriteria.getValue()) :
                        builder.equal(getJoin(root, searchCriteria), searchCriteria.getValue());
            }

            case OPERATION_JOIN_NOT_EQUAL: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.notEqual(getJoin(root, searchCriteria).get(fields[fields.length - 1]), searchCriteria.getValue()) :
                        builder.notEqual(getJoin(root, searchCriteria), searchCriteria.getValue());
            }

            case OPERATION_JOIN_LIKE: {
                String value = "%" + searchCriteria.getValue().toString().toLowerCase() + "%";
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.like(builder.lower(getJoin(root, searchCriteria).get(fields[fields.length - 1]).as(String.class)), value) :
                        builder.like(builder.lower(getJoin(root, searchCriteria).as(String.class)), value);
            }

            case OPERATION_JOIN_EQUAL_STRING: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.equal(getJoin(root, searchCriteria).get(fields[fields.length - 1]).as(String.class), searchCriteria.getValue()) :
                        builder.equal(getJoin(root, searchCriteria).as(String.class), searchCriteria.getValue());

            }

            case OPERATION_JOIN_IN: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.in(getJoin(root, searchCriteria).get(fields[fields.length - 1])).value(searchCriteria.getValue()) :
                        builder.in(getJoin(root, searchCriteria)).value(searchCriteria.getValue());
            }

            case OPERATION_JOIN_NULL: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.isNull(getJoin(root, searchCriteria).get(fields[fields.length - 1])) :
                        builder.isNull(getJoin(root, searchCriteria));
            }

            case OPERATION_JOIN_EMPTY: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.isEmpty(getJoin(root, searchCriteria).get(fields[fields.length - 1])) :
                        builder.isEmpty(getJoin(root, searchCriteria));
            }

            case OPERATION_JOIN_NOT_NULL: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.isNotNull(getJoin(root, searchCriteria).get(fields[fields.length - 1])) :
                        builder.isNotNull(getJoin(root, searchCriteria));
            }

            case OPERATION_JOIN_NOT_EMPTY: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.isNotEmpty(getJoin(root, searchCriteria).get(fields[fields.length - 1])) :
                        builder.isNotEmpty(getJoin(root, searchCriteria));
            }

            case OPERATION_YEAR: {
                Expression<Long> function = builder.function("YEAR", Long.class, getPath(root, searchCriteria));
                return builder.equal(function, Long.valueOf(searchCriteria.getValue().toString()));
            }

            case OPERATION_JOIN_YEAR: {
                String[] fields = searchCriteria.getKey().split(SEPARATOR);
                return fields.length > 1 ?
                        builder.equal(builder.function("YEAR", Long.class, getJoin(root, searchCriteria).get(fields[fields.length -1])), Long.valueOf(searchCriteria.getValue().toString())) :
                        builder.equal(builder.function("YEAR", Long.class, getJoin(root, searchCriteria)), Long.valueOf(searchCriteria.getValue().toString()));
            }

            default: {
                logger.info("handling for this operation [" + searchCriteria.getOperation() + "] is not found!");

                return null;
            }
        }
    }

    private Predicate getPredicate(MultipleCriteria multipleCriteria, Root<T> root, CriteriaBuilder builder) {
        Predicate predicateInner = null;
        List<Predicate> predicates = new ArrayList<>();

        if (multipleCriteria.getInner() != null && !multipleCriteria.getInner().getIsProcessed()) {
            predicateInner = getPredicate(multipleCriteria.getInner(), root, builder);
        }

        for (MultipleCriteria.Criteria criteria : multipleCriteria.getCriterias()) {
            List<Predicate> p = new ArrayList<>();

            for (SearchCriteria searchCriteria : criteria.getSearchCriterias()) {
                p.add(getPredicate(searchCriteria, root, builder));
            }

            Predicate[] p2 = p.toArray(new Predicate[p.size() - 1]);

            predicates.add(criteria.getOperator().equals(SearchCriteria.OPERATOR_AND) ? builder.and(p2) : builder.or(p2));
        }

        multipleCriteria.setIsProcessed(true);

        Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size() - 1]);
        Predicate predicatesProcessed = multipleCriteria.getOperatorCriteria().equalsIgnoreCase(SearchCriteria.OPERATOR_AND) ? builder.and(predicatesArray) : builder.or(predicatesArray);
        Predicate predicate = predicatesProcessed;

        if (multipleCriteria.getInner() != null) {
            predicate = multipleCriteria.getOperatorInner().equals(SearchCriteria.OPERATOR_AND) ? builder.and(predicatesProcessed, predicateInner) : builder.or(predicatesProcessed, predicateInner);
        }

        return predicate;
    }

    private Path<?> getPath(Root<T> root, SearchCriteria criteria) {
        String[] attributes = criteria.getKey().split(SEPARATOR);
        Path<?> field = root.get(attributes[0]);

        for (int i = 1; i < attributes.length; i++) {
            field = field.get(attributes[i]);
        }

        return field;
    }

    private Join getJoin(Root<T> root, SearchCriteria criteria) {
        String[] attributes = criteria.getKey().split(SEPARATOR);
        Join joinTable = root.join(attributes[0], JoinType.LEFT);

        for (int i = 1; i < attributes.length - 1; i++) {
            joinTable = joinTable.join(attributes[i], JoinType.LEFT);
        }

        return joinTable;
    }
}
