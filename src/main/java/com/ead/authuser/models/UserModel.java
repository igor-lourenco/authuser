package com.ead.authuser.models;

import com.ead.authuser.controllers.UserController;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Entity
@Table(name = "TB_USERS")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignora campos com valores nulos durante a serialização para JSON
public class UserModel extends RepresentationModel<UserModel> implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 255)
    @JsonIgnore  // exclui campo da serialização e desserialização para JSON
    private String password;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String cpf;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") // Padrão ISO 8601 UTC
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") // Padrão ISO 8601 UTC
    private LocalDateTime lastUpdateDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Configuração de acesso que significa que essa propriedade só pode ser escrita (set) para desserialização, mas não será lida (get) na serialização, ou seja, o valor da propriedade não é incluído na serialização.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // // Campo que está a chave estrangeira na outra tabela para referenciar esse course e carregamento lento(FetchType.LAZY)
    private Set<UserCourseModel> usersCourses;


    @JsonInclude(JsonInclude.Include.NON_EMPTY) // Ignora campo com valor vazio durante a serialização para JSON
    @Override
    public Links getLinks() {
        return super.getLinks();
    }

    /** Adiciona link do hateoas */
    public void addLinks() {
        add(     // Adiciona o link fornecido ao recurso.
                linkTo(
                        methodOn(
                                UserController.class)     // controller onde está o método
                                .getOneUser(userId)    // método
                ).withSelfRel());     // qualifica qual é a relação desse link com o recurso, nesse caso auto-relação
    }
}
