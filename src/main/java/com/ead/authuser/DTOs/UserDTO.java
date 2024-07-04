package com.ead.authuser.DTOs;

import com.ead.authuser.services.valid.EmailUpdateValid;
import com.ead.authuser.services.valid.UserNameValid;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    public interface UserView {
        // Obs: se o atributo do UserDTO estiver mapeado, mas na View não correspondente, a annotation JsonView ignora o campo e salva com valor null
        public static interface RegistrationPost {
        } // UserDTO Para registrar usuário

        public static interface UserPut {
        } // UserDTO para atualizar os dados do usuário

        public static interface PasswordPut {
        } // UserDTO para atualizar a senha do usuário

        public static interface ImagePut {
        } // UserDTO para atualizar a imagem do usuário
    }

    private UUID userId;

    @JsonView(UserView.RegistrationPost.class)
    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50)
    @UserNameValid(groups = UserView.RegistrationPost.class)
    private String username;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class)
    @EmailUpdateValid(groups = UserView.UserPut.class)
    private String email;

    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 10, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @JsonView(UserView.PasswordPut.class)
    @NotBlank(groups = UserView.PasswordPut.class)
    @Size(min = 6, max = 10, groups = UserView.PasswordPut.class)
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    @Pattern(regexp = "(\\d{3}.?\\d{3}.?\\d{3}-?\\d{2})", message = "Campo 'cpf' inválido, padrão aceito 99999999999 ou 999.999.999-99", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @NotBlank(groups = UserView.ImagePut.class)
    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
