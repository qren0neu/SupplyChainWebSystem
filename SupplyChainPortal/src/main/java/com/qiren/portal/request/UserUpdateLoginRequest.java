package com.qiren.portal.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateLoginRequest {

    @NotBlank(message = "Please enter your username")
    @Pattern(regexp = "[a-zA-Z0-9]{6,12}", message = "Invalid username format")
    private String username;

    @NotBlank(message = "Please enter your password")
    @Size(min = 8, max = 24, message = "Invalid password length")
    private String password;
}
