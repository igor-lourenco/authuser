package com.ead.authuser.services.valid.validator;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.valid.EmailUpdateValid;
import com.ead.authuser.services.valid.UserNameValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class EmailUpdateValidator implements ConstraintValidator<EmailUpdateValid, String> {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(EmailUpdateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        var userId = uriVars.get("userId");

        Optional<UserModel> userEntity = repository.findById(UUID.fromString(userId));
        Optional<UserModel> userByEmail = repository.findByEmail(email);

        // Se o email já existir e o user não for igual ao user do email, retorna falso, ou seja, retorna erro na validação
        if (userByEmail.isPresent() && !userEntity.get().getUserId().equals(userByEmail.get().getUserId())) {
            return false;
        }

        return true;
    }
}
