package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotSpacesCustomValidator.class)
public @interface NotSpacesCustom {

    String message() default "Логин должен быть без пробелов";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
