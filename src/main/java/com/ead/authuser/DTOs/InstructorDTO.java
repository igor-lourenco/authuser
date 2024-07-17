package com.ead.authuser.DTOs;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class InstructorDTO {

//    @NotBlank // É projetada para ser usada em tipos de String e não UUID
    @NotNull
    private UUID userId;
}
