package com.ead.authuser.controllers;

import com.ead.authuser.DTOs.InstructorDTO;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserServiceInterface;
import com.ead.authuser.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@RequestMapping("/instructors")
@Log4j2
public class InstructorController {

    @Autowired
    UserServiceInterface userService;
    @Autowired
    LogUtils logUtils;

    @PostMapping(value = "/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDTO instructorDTO){
        log.info("REQUEST - POST [saveSubscriptionInstructor] : BODY: {}", logUtils.convertObjectToJson(instructorDTO));

        Optional<UserModel> userModelOptional = userService.findById(instructorDTO.getUserId());

        if(userModelOptional.isEmpty()){
            log.warn("RESPONSE - POST [saveSubscriptionInstructor] : User not found :: {}", instructorDTO.getUserId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        var userModel = userModelOptional.get();
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);

        log.info("RESPONSE - POST [saveSubscriptionInstructor] : BODY: {}", logUtils.convertObjectToJson(userModel));
        return ResponseEntity.status(HttpStatus.OK).body(userModel);

    }


}
