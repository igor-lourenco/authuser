package com.ead.authuser.controllers;

import com.ead.authuser.DTOs.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserServiceInterface;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.ead.authuser.utils.LogUtils;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@RequestMapping("/users")
@Log4j2
public class UserController {

    @Autowired
    UserServiceInterface userService;
    @Autowired
    LogUtils logUtils;


    @GetMapping
    public ResponseEntity<Page<UserModel>> findAllUsersPaged(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0, size = 12, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "courseId", required = false) UUID courseId) {
        log.info("REQUEST - GET [findAllUsers] PARAMS :: courseId: {} - PAGED: {}", courseId, pageable.toString());

        Page<UserModel> userModelPage = null;

        if (courseId != null) {
            Specification<UserModel> userCourseSpecification = SpecificationTemplate.userCourseId(courseId).and(spec);
            userModelPage = userService.findAllPaged(userCourseSpecification, pageable);

        }else{
            userModelPage = userService.findAllPaged(spec, pageable);
        }

        userModelPage.forEach(user -> user.addLinks());

        String pageJson = logUtils.convertObjectToJson(userModelPage);
        log.info("RESPONSE - GET [findAllUsers] : {}", pageJson);

        return ResponseEntity.ok().body(userModelPage);
    }


    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> getOneUser(@PathVariable(value = "userId") UUID userId) {
        log.info("REQUEST - GET [getOneUser] PARAMS : {}", userId);

        Optional<UserModel> entity = userService.findById(userId);

        if (entity.isEmpty()) {
            log.warn("RESPONSE - GET [getOneUser] : User not found :: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");

        } else {
            log.info("RESPONSE - GET [getOneUser] : {}", logUtils.convertObjectToJson(entity.get()));
            return ResponseEntity.ok(entity.get());
        }
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.UserPut.class) @Validated(UserDTO.UserView.UserPut.class) UserDTO userDTO) {
        log.info("REQUEST - PUT [updateUser] PARAMS :: userId: {} - BODY: {}", userId, logUtils.convertObjectToJson(userDTO));

        Optional<UserModel> entityDTO = userService.findById(userId);

        if (entityDTO.isEmpty()) {
            log.warn("RESPONSE - PUT [updateUser] : User not found :: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");

        } else {
            var userModel = entityDTO.get();
            userModel.setFullName(userDTO.getFullName());
            userModel.setPhoneNumber(userDTO.getPhoneNumber());
            userModel.setEmail(userDTO.getEmail());
            userModel.setCpf(userDTO.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);

            log.info("RESPONSE - PUT [updateUser] : {}", logUtils.convertObjectToJson(userModel));
            return ResponseEntity.ok(userModel);
        }
    }

    @PutMapping(value = "/{userId}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.PasswordPut.class) @Validated(UserDTO.UserView.PasswordPut.class) UserDTO userDTO) {
        log.info("REQUEST - PUT [updatePassword] PARAMS :: userId: {} - BODY: {}", userId, logUtils.convertObjectToJson(userDTO));


        Optional<UserModel> entityDTO = userService.findById(userId);

        if (entityDTO.isEmpty()) {
            log.warn("RESPONSE - PUT [updatePassword] : User not found :: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        if (!userDTO.getOldPassword().equals(entityDTO.get().getPassword())) {
            log.warn("RESPONSE - PUT [updatePassword] : Mismatched old password!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");

        } else {
            var userModel = entityDTO.get();
            userModel.setPassword(userDTO.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);

            log.info("RESPONSE - PUT [updatePassword] : Password updated successfully!");
            return ResponseEntity.ok("Password updated successfully!");
        }
    }

    @PutMapping(value = "/{userId}/image")
    public ResponseEntity<?> updateImage(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.ImagePut.class) @Validated(UserDTO.UserView.ImagePut.class) UserDTO userDTO) {
        log.info("REQUEST - PUT [updateImage] PARAMS :: userId: {} - BODY: {}", userId, logUtils.convertObjectToJson(userDTO));

        Optional<UserModel> entityDTO = userService.findById(userId);

        if (entityDTO.isEmpty()) {
            log.warn("RESPONSE - PUT [updateImage] : User not found :: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        } else {
            var userModel = entityDTO.get();
            userModel.setImageUrl(userDTO.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);

            log.info("RESPONSE - PUT [updateImage] : {}", logUtils.convertObjectToJson(userModel));
            return ResponseEntity.ok(userModel);
        }
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") UUID userId) {
        log.info("REQUEST - DELETE [deleteUser] PARAMS :: userId: {} ", userId);

        Optional<UserModel> entity = userService.findById(userId);

        if (entity.isEmpty()) {
            log.warn("RESPONSE - DELETE [deleteUser] : User not found :: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");

        } else {
            userService.deleteUser(entity.get());

            log.info("RESPONSE - DELETE [deleteUser] : User deleted successfully!");
            return ResponseEntity.ok("User deleted successfully");
        }
    }
}
