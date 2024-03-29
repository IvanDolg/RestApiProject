package com.example.restapiproject.dto.UserDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class LoginUserDto {
    @NotEmpty
    @NotBlank
    private Long id;

    @NotEmpty
    @NotBlank
    private String username;

    @NotEmpty
    @NotBlank
    @Range(min = 4,max = 16)
    private String password;

    @NotEmpty
    @NotBlank
    private LocalDateTime lastVisitDate;
}
