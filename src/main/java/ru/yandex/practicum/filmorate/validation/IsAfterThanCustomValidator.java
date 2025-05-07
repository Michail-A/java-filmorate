package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class IsAfterThanCustomValidator implements ConstraintValidator<IsAfterThanCustom, LocalDate> {

    LocalDate minDate;

    @Override
    public void initialize(IsAfterThanCustom constraintAnnotation) {
        minDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date == null || !date.isBefore(minDate);
    }
}
