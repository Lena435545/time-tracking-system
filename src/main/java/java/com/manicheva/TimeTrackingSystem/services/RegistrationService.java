package java.com.manicheva.TimeTrackingSystem.services;

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
    public void register(Account account){

        User user = userRepository.findByEmail(account.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole(Role.USER);
        account.setUser(user);
        accountRepository.save(account);
    }
}
