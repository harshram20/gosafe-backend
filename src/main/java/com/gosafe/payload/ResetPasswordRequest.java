package com.gosafe.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String securityAnswer;

    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
