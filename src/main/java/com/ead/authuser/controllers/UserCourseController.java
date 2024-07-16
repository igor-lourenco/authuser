package com.ead.authuser.controllers;

import com.ead.authuser.DTOs.CourseDTO;
import com.ead.authuser.clients.CourseRequestClient;
import com.ead.authuser.utils.LogUtils;
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
    CourseRequestClient courseRequestClient;
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
}
