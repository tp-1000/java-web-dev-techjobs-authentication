package org.launchcode.javawebdevtechjobsauthentication.models;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User extends AbstractEntity{

    @NotNull
    private String userName;

    @NotNull
    private String passwordHash;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User() {
    }


    public User(String userName, String rawPassword) {
        this.userName = userName;
        this.passwordHash = encodePassword(rawPassword);

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getpasswordHashHash() {
        return passwordHash;
    }

    public void setpasswordHash(String rawpassword) {
        this.passwordHash = rawpassword;
    }

    public String encodePassword(String password) {
        String passwordHash = bCryptPasswordEncoder.encode(password);
        return passwordHash;
    }

    public boolean isPasswordMatch(String password) {
        return bCryptPasswordEncoder.matches(password, passwordHash);
    }

}
