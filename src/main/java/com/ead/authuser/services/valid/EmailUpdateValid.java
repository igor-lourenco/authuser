package com.ead.authuser.services.valid;

import com.ead.authuser.services.valid.validator.EmailUpdateValidator;
import com.ead.authuser.services.valid.validator.UserNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/** Se o email já existir e o user não for igual ao user do email, retorna falso, ou seja, retorna erro na validação */
@Documented
@Constraint(validatedBy = EmailUpdateValidator.class) // Classe que vai ter a implementação
@Target({ElementType.METHOD, ElementType.FIELD}) // Onde vai ser utilizado essa anotação
@Retention(RetentionPolicy.RUNTIME)    // Definir quando que essa validação vai ocorrer, nesse caso em tempo de execução
public @interface EmailUpdateValid {

    String message() default "Campo 'email' inválido, email já existe";   // mensagem padrão, quando ocorrer esse erro de validação
    Class<?>[] groups() default {};                // Grupo de validação, caso precise definir
    Class<? extends Payload>[] payload() default {}; // Nível que vai ocorrer o erro de validação
}
