package com.ead.authuser.controllers;

import com.ead.authuser.DTOs.UserDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserServiceInterface;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserServiceInterface userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerUser(
            @RequestBody @JsonView(UserDTO.UserView.RegistrationPost.class) @Validated(UserDTO.UserView.RegistrationPost.class) UserDTO userDTO) {

        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is already taken!");
        }

        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is already taken!");
        }

        var entityModel = new UserModel();
        BeanUtils.copyProperties(userDTO, entityModel);
        entityModel.setUserStatus(UserStatus.ACTIVE);
        entityModel.setUserType(UserType.STUDENT);
        entityModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        entityModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(entityModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);

    }

}
