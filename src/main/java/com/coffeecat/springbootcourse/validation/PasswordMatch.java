package com.coffeecat.springbootcourse.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({TYPE}) //validates SiteUser Type
@Retention(RUNTIME) //Run at runtime
@Constraint(validatedBy=PasswordMatchValidator.class)//tell Annotation to use PasswordMatchValidator class:
@Documented //make documentation pop-up
public @interface PasswordMatch {
    //THIS IS REQUIRED SYNTAX:
    String message() default "{error.password.mismatch}"; //Default error-code
    //allows specifying groups of different validation constraints that can run at different times.
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
