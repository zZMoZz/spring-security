package com.zzz.spring_security.config;

import com.zzz.spring_security.model.Account;
import com.zzz.spring_security.model.Authority;
import com.zzz.spring_security.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "This account not found for user: " + username));

        // extract the roles of this account (user)
        String[] roles = account.getAuthorities().stream().map(Authority::getName).toArray(String[]::new);

        return User.builder()
                .username(account.getEmail())
                .password(account.getPwd())
                .authorities(roles) // if you use "roles()" method, will return an error.
                .build();           // because roles add "ROLE_" prefix, so roles() method
                                    // want roles written in this format "USER" not "ROLE_USER".
    }
}
