package com.ead.authuser.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    public interface UserView{
        public static interface RegistrationPost{} // UserDTO Para registrar usuário
        public static interface UserPut{} // UserDTO para atualizar os dados do usuário
        public static interface PasswordPut{} // UserDTO para atualizar a senha do usuário
        public static interface ImagePut{} // UserDTO para atualizar a imagem do usuário
    }

    private UUID userId;

    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
