package com.ead.authuser.controllers;

import com.ead.authuser.DTOs.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserServiceInterface;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)  // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceInterface userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> findAllUsers(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0, size = 12, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<UserModel> userModelPage = userService.findAllPaged(spec, pageable);

        return ResponseEntity.ok().body(userModelPage);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> getOneUser(@PathVariable(value = "userId") UUID userId) {

        Optional<UserModel> entity = userService.findById(userId);

        if (entity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");

        } else {

            return ResponseEntity.ok(entity.get());
        }
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.UserPut.class) @Validated(UserDTO.UserView.UserPut.class) UserDTO userDTO) {

        Optional<UserModel> entityDTO = userService.findById(userId);

        if (entityDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");

        } else {
            var userModel = entityDTO.get();
            userModel.setFullName(userDTO.getFullName());
            userModel.setPhoneNumber(userDTO.getPhoneNumber());
            userModel.setEmail(userDTO.getEmail());
            userModel.setCpf(userDTO.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);

            return ResponseEntity.ok(userModel);
        }
    }

    @PutMapping(value = "/{userId}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.PasswordPut.class) @Validated(UserDTO.UserView.PasswordPut.class) UserDTO userDTO) {

        Optional<UserModel> entityDTO = userService.findById(userId);

        if (entityDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        if (!userDTO.getOldPassword().equals(entityDTO.get().getPassword())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        } else {
            var userModel = entityDTO.get();
            userModel.setPassword(userDTO.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);

            return ResponseEntity.ok("Password updated successfully!");
        }
    }

    @PutMapping(value = "/{userId}/image")
    public ResponseEntity<?> updateImage(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.ImagePut.class) @Validated(UserDTO.UserView.ImagePut.class) UserDTO userDTO) {

        Optional<UserModel> entityDTO = userService.findById(userId);

        if (entityDTO.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        } else {
            var userModel = entityDTO.get();
            userModel.setImageUrl(userDTO.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);

            return ResponseEntity.ok(userModel);
        }
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> entity = userService.findById(userId);

        if (entity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");

        } else {
            userService.deleteUser(entity.get());

            return ResponseEntity.ok("User deleted successfully");
        }
    }
}
