package com.ead.authuser.controllers;

import com.ead.authuser.DTOs.CourseDTO;
import com.ead.authuser.DTOs.UserCourseDTO;
import com.ead.authuser.clients.CourseRequestClient;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseServiceInterface;
import com.ead.authuser.services.UserServiceInterface;
import com.ead.authuser.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@Log4j2
public class UserCourseController {

    @Autowired
    CourseRequestClient courseRequestClient;
    @Autowired
    UserServiceInterface userService;
    @Autowired
    UserCourseServiceInterface userCourseService;
    @Autowired
    LogUtils logUtils;
    
    @GetMapping(value = "/users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> findAllCoursesByUserPaged(
            @PageableDefault(page = 0, size = 12, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId){
        log.info("REQUEST - GET [findAllCoursesByUserPaged] PARAMS :: userId: {} - PAGED: {}", userId, pageable.toString());

        Page<CourseDTO> coursesByUserPaged = courseRequestClient.getAllCoursesByUserPaged(userId, pageable);

        String pageJson = logUtils.convertObjectToJson(coursesByUserPaged);
        log.info("RESPONSE - GET [findAllCoursesByUserPaged] : {}", pageJson);
        return ResponseEntity.ok().body(coursesByUserPaged);
    }

    @PostMapping(value = "/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "userId") UUID userId,
            @RequestBody @Valid UserCourseDTO userCourseDTO){
        log.info("REQUEST - POST [saveSubscriptionUserInCourse] PARAMS :: userId: {} - BODY: {}", userId, userCourseDTO);

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            log.warn("RESPONSE - POST [saveSubscriptionUserInCourse] : User not found :: {}", userId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        if(userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseDTO.getCourseId())){
            log.warn("RESPONSE - POST [saveSubscriptionUserInCourse] : Subscription already exists! :: user: {} - courseId: {}", userModelOptional.get(), userCourseDTO.getCourseId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: subscription already exists!");
        }

        UserCourseModel userCourseModel = userModelOptional.get().convertToUserCourseModel(userCourseDTO.getCourseId());
        userCourseModel = userCourseService.save(userCourseModel);

        log.info("RESPONSE - POST [saveSubscriptionUserInCourse] : {}", logUtils.convertObjectToJson(userCourseModel));
        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);

    }
}
