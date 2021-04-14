package com.blog.demo.model.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

    private long minAge;
    private long maxAge;

    @Override
    public void initialize(Age age) {
        this.minAge = age.min();
        this.maxAge = age.max();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(localDate) &&
                isGreaterThanOrEqualToMin(localDate) &&
                isLessThanOrEqualToMax(localDate);
    }

    private boolean isLessThanOrEqualToMax(LocalDate localDate) {
        return ChronoUnit.YEARS.between(localDate, LocalDate.now()) <= maxAge;
    }

    private boolean isGreaterThanOrEqualToMin(LocalDate localDate) {
        return ChronoUnit.YEARS.between(localDate, LocalDate.now()) >= minAge;
    }
}
