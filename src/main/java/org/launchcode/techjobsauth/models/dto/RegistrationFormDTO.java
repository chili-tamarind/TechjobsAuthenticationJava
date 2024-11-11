package org.launchcode.techjobsauth.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistrationFormDTO extends LoginFormDTO{

    @NotNull(message = "Verify your password")
    @NotBlank(message = "Verify your password")
    @Size(min=5, max=50, message = "Password must be 5-50 characters")
    private String verifyPassword;

    public String getVerifyPassword() { return verifyPassword;}
    public void setVerifyPassword(String verifyPassword) { this.verifyPassword = verifyPassword;}
}
