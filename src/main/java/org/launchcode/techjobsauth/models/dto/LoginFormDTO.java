package org.launchcode.techjobsauth.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginFormDTO {

    @NotNull(message = "Username is required")
    @NotBlank(message = "Username cannot be blank")
    @Size(min=5, max=50, message = "Username must be 5-50 characters")
    private String username;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password cannot be blank")
    @Size(min=5, max=50, message = "Password must be 5-50 characters")
    private String password;

    public String getUsername() { return username;}
    public void setUsername(String username) { this.username = username;}

    public String getPassword() { return password;}
    public void setPassword(String password) { this.password = password;}
}