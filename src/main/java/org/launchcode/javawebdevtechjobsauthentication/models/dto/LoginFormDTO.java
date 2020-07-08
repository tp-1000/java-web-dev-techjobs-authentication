package org.launchcode.javawebdevtechjobsauthentication.models.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginFormDTO {

    @Valid
    @NotBlank
    @Size(min = 3, max = 25, message = "Must have 3 to 25 characters")
    private String userName;

    @Valid
    @NotBlank
    @Size(min = 3, max = 25, message = "Must have 3 to 25 characters")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
