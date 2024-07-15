package com.ead.authuser.controllers;

import com.ead.authuser.DTOs.CourseDTO;
import com.ead.authuser.clients.UserRequestClient;
import com.ead.authuser.models.UserModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@Log4j2
public class UserCourseController {

    @Autowired
    UserRequestClient userRequestClient;
    
    @GetMapping(name = "/users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> findAllCoursesByUserPaged(
            @PageableDefault(page = 0, size = 12, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId){

        Page<CourseDTO> coursesByUserPaged = userRequestClient.getAllCoursesByUserPaged(userId, pageable);


        return ResponseEntity.ok().body(coursesByUserPaged);
    }
}
