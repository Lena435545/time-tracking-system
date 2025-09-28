package java.com.manicheva.TimeTrackingSystem.controllers;

import com.manicheva.TimeTrackingSystem.models.Account;
import com.manicheva.TimeTrackingSystem.repositories.AccountRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final AccountRepository accountRepository;

    public HomeController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        Account account = accountRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        model.addAttribute("user", account.getUser());
        return "home/home";
    }
}
