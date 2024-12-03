package com.zzz.spring_security.Config;

import com.zzz.spring_security.model.Account;
import com.zzz.spring_security.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "This account not found for user: " + username));

        return User.builder()
                .username(account.getEmail())
                .password(account.getPwd())
                .roles(account.getRole())
                .build();
    }
}
