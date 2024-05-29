package com.maveric.tsp.registerservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
public @interface PasswordValidation {
    String message() default "Password didn't not meet security requirements.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
