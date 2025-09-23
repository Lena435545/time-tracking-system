package com.manicheva.TimeTrackingSystem.controllers;

import com.manicheva.TimeTrackingSystem.models.Account;
import com.manicheva.TimeTrackingSystem.models.User;
import com.manicheva.TimeTrackingSystem.services.RegistrationService;
import com.manicheva.TimeTrackingSystem.utils.AccountValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AccountValidator accountValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(AccountValidator accountValidator, RegistrationService registrationService) {
        this.accountValidator = accountValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(@ModelAttribute("account") Account account) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registerAccount(@ModelAttribute("account") @Valid Account account,
                                      BindingResult bindingResult) {
        accountValidator.validate(account, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(account);

        return "redirect:/auth/login";
    }

    @GetMapping("/access_denied")
    public String showAccessDeniedPage(){
        return "auth/access_denied";
    }

}
