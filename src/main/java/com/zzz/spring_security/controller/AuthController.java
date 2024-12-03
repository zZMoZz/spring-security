package com.zzz.spring_security.controller;

import com.zzz.spring_security.model.Account;
import com.zzz.spring_security.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;

    /* We don't handle if email is repeated from users case */

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Account account) {
        /* Several errors maybe occur, like client pass attribute in wrong format */
        try {
            // Hash the password
            String hashPwd = passwordEncoder.encode(account.getPwd());
            account.setPwd(hashPwd);

            // Save this user to database
            Account savedAccount = accountRepo.save(account);

            // return a response
            if (savedAccount.getId()>0)
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("New user successfully registered");
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User registeration failed");

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }
    }
}
