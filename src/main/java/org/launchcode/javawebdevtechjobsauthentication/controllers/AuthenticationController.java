package org.launchcode.javawebdevtechjobsauthentication.controllers;


import org.launchcode.javawebdevtechjobsauthentication.models.User;
import org.launchcode.javawebdevtechjobsauthentication.models.data.UserRepository;
import org.launchcode.javawebdevtechjobsauthentication.models.dto.LoginFormDTO;
import org.launchcode.javawebdevtechjobsauthentication.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String USERSESSIONKEY = "default user";

    public User getUserFromSession(HttpSession session) {

        Integer userId = (Integer) session.getAttribute(USERSESSIONKEY);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(USERSESSIONKEY, user.getId());
    }

    @GetMapping("/login")
    public String getLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        return "/login";
    }

    @PostMapping("/login")

    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO, Errors error, HttpServletRequest request) {
        if(error.hasErrors()){
            return "/login";
        }

        User theUser = userRepository.findByuserName(loginFormDTO.getUserName());
        if(theUser == null) {
            error.rejectValue("userName", "user.invalid", "The given username does not exist");

            return "/login";
        }

        String password = loginFormDTO.getPassword();

        if(!theUser.isPasswordMatch(password)) {
            error.rejectValue("password", "password.invalid", "Invalid password");
            return "/login";
        }

        setUserInSession(request.getSession(), theUser);
        return "redirect:";
    }



    @GetMapping("/register")
    public String getRegisterForm(Model model){
        model.addAttribute(new RegisterFormDTO());

        return "/register";
    }

    @PostMapping("/register")
    public String processRegisterForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO, Errors error, HttpServletRequest request){

        if(error.hasErrors()) {
            //maybe need to have model updated with formDTO
            return "/register";
        }

        User existingUser = userRepository.findByuserName(registerFormDTO.getUserName());
        if(existingUser != null) {
            error.rejectValue("userName", "username.alreadyexists", "A user with that username already exists");

            return "/register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if(!password.equals(verifyPassword)) {
            error.rejectValue("password", "password.mismatch", "Passwords do not match");
            return "/register";
        }

        User newUser = new User(registerFormDTO.getUserName(), registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);
        return "/index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect/login";
    }
}
