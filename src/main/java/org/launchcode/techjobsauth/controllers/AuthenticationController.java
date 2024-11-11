package org.launchcode.techjobsauth.controllers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.launchcode.techjobsauth.models.User;
import org.launchcode.techjobsauth.models.data.UserRepository;
import org.launchcode.techjobsauth.models.dto.LoginFormDTO;
import org.launchcode.techjobsauth.models.dto.RegistrationFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        // User details not entered
        if(userId == null){
            return null;
        }

        // User doesn't exists
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            return null;
        }

        return userOptional.get();

    }

    // REGISTRATION
    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegistrationFormDTO());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegistrationFormDTO registrationFormDTO,
                                          Errors errors, HttpServletRequest request, Model model) {
        //HttpServletRequest not HttpServlet?

        if (errors.hasErrors()){
            return "register";
        }

        User extistingUser = userRepository.findByUsername(registrationFormDTO.getUsername());
        if (extistingUser != null){
            errors.rejectValue("username",
                            "username.alreadyExits",
                        "That username is not available");
            return  "register";
        }

        String password = registrationFormDTO.getPassword();
        String verifyPassword = registrationFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)){
            errors.rejectValue("verifyPassword",
                    "password.mismatch",
                    "Passwords do not match!");
            return  "register";
        }

        User newUser = new User(registrationFormDTO.getUsername(),registrationFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);
        return "redirect:";
    }

    // LOGIN
    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                          Errors errors, HttpServletRequest request, Model model) {
        //HttpServletRequest not HttpServlet?

        if (errors.hasErrors()){
            return "login";
        }

        User extistingUser = userRepository.findByUsername(loginFormDTO.getUsername());
        String password = loginFormDTO.getPassword();

        if (extistingUser == null || !extistingUser.isMatchingPassword(password)){
            errors.rejectValue("password",
                    "password.invalid",
                    "Invalid username or password. Please try again.");
            return  "login";
        }

        // CHECKER - redact
//        if (extistingUser == null){
//            errors.rejectValue("username",
//                    "username.invalid",
//                    "Invalid username");
//            return  "login";
//        }
//
//        if (!extistingUser.isMatchingPassword(password)){
//            errors.rejectValue("password",
//                    "password.invalid",
//                    "Invalid password");
//            return  "login";
//        }

        setUserInSession(request.getSession(), extistingUser);
        return "redirect:";
    }

    // LOGIN
    @GetMapping("/logout")
    public String displayLogout(HttpServletRequest request) {
        request.getSession().invalidate();

        //return "redirect:/logout";
        return "logout";
    }
}