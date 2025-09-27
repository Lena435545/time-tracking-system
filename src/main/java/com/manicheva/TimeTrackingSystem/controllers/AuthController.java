package com.manicheva.TimeTrackingSystem.controllers;

import com.manicheva.TimeTrackingSystem.models.Account;
import com.manicheva.TimeTrackingSystem.services.RegistrationService;
import com.manicheva.TimeTrackingSystem.utils.AccountValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        accountValidator.validate(account, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        try {
            registrationService.register(account);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("username", "", e.getMessage());
            return "/auth/registration";
        } catch (Exception e) {
            bindingResult.rejectValue("username", "", "Registration failed. Please try again.");
            return "/auth/registration";
        }

        return "redirect:/auth/login";
    }

    @GetMapping("/access_denied")
    public String showAccessDeniedPage(){
        return "auth/access_denied";
    }

}
