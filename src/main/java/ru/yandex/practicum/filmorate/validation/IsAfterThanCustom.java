package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsAfterThanCustomValidator.class)
public @interface IsAfterThanCustom {

    String message() default "Дата не может быть раньше {value}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String value() default "";
}
