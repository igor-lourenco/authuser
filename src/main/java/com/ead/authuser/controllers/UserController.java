package com.ead.authuser.controllers;

import com.ead.authuser.DTOs.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserServiceInterface;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceInterface userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> findAllUsers(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> getOneuser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> entity = userService.findById(userId);

        if(entity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }else{
            return ResponseEntity.ok(entity.get());
        }
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.UserPut.class) UserDTO userDTO){

        Optional<UserModel> entityDTO = userService.findById(userId);

        if(entityDTO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }else{

            var userModel = entityDTO.get();
            userModel.setFullName(userDTO.getFullName());
            userModel.setPhoneNumber(userDTO.getPhoneNumber());
            userModel.setCpf(userDTO.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            userService.save(userModel);

            return ResponseEntity.ok(userModel);
        }
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> entity = userService.findById(userId);

        if(entity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }else{
            userService.deleteUser(entity.get());
            return ResponseEntity.ok("User deleted successfully");
        }
    }
}
