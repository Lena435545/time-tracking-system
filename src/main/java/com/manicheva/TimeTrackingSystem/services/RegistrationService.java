package com.manicheva.TimeTrackingSystem.services;

import com.manicheva.TimeTrackingSystem.models.Account;
import com.manicheva.TimeTrackingSystem.models.Role;
import com.manicheva.TimeTrackingSystem.models.User;
import com.manicheva.TimeTrackingSystem.repositories.AccountRepository;
import com.manicheva.TimeTrackingSystem.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(AccountRepository accountRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Account account) {
        // Check if user already exists
        if (userRepository.findByEmail(account.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Create new user
        User user = new User();
        user.setEmail(account.getUsername());
        // Set default values - these should be collected during registration
        user.setFirstName("User");
        user.setLastName("Name");
        user.setDepartment("General");
        
        User savedUser = userRepository.save(user);

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole(Role.USER);
        account.setUser(savedUser);
        accountRepository.save(account);
    }
}
