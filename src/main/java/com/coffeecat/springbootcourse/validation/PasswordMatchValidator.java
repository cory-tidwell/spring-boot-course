package com.coffeecat.springbootcourse.validation;

import com.coffeecat.springbootcourse.model.entity.SiteUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//implements Constraint Interface < CustomAnnotation, ObjectToValidate >
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, SiteUser> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SiteUser siteUser, ConstraintValidatorContext constraintValidatorContext) {
        //get passwords:
        String plainPassword = siteUser.getPlainPassword();
        String repeatPassword = siteUser.getRepeatPassword();

        //only run validation process registering Account: - FILTER -
        if(plainPassword == null || plainPassword.length() == 0) {
            return true; //not registration process!
        }
        //validate the repeat Password during registration:
        if(plainPassword == null || !plainPassword.equals(repeatPassword)) {
            return false;
        }

        return true; //validation is successful.
    }
}
