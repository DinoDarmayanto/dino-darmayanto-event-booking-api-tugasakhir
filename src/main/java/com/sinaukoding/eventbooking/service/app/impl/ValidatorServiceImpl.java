package com.sinaukoding.eventbooking.service.app.impl;

import com.sinaukoding.eventbooking.service.app.ValidatorService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidatorServiceImpl implements ValidatorService {

    private final Validator validator;

    @Override
    public void validator(Object request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

}
