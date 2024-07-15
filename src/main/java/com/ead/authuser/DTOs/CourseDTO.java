package com.ead.authuser.DTOs;

import com.ead.authuser.enums.CourseLevel;
import com.ead.authuser.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CourseDTO {

    private UUID courseId;
    private String name;
    private String description;
    private CourseStatus courseStatus;
    private CourseLevel courseLevel;
    private UUID userInstructor;

    @JsonIgnore
    private String imageUrl;

    @JsonIgnore
    private String creationDate;

    @JsonIgnore
    private String lastUpdateDate;
}
