package com.ead.authuser.services.valid.validator;

import com.ead.authuser.services.valid.UserNameValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameValidator implements ConstraintValidator<UserNameValid, String> {
    @Override
    public void initialize(UserNameValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {

        // Se for null, ou vazio, ou tiver espaços em branco, retorna falso, ou seja, retorna erro na validação
        if (username == null || username.trim().isEmpty() || username.contains(" ")) {
            return false;
        }
        return true;

    }
}
