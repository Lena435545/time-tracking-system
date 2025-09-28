package java.com.manicheva.TimeTrackingSystem.utils;

import com.manicheva.TimeTrackingSystem.models.Account;
import com.manicheva.TimeTrackingSystem.repositories.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    private final AccountRepository accountRepository;

    public AccountValidator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Account.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    Account account = (Account) target;

    accountRepository.findByUsername(account.getUsername()).ifPresent(existingAccount ->
            errors.rejectValue("username","", "This username already exists"));
    }
}
